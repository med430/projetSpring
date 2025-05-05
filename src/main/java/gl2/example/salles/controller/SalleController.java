package gl2.example.salles.controller;

import gl2.example.salles.dto.CalendarDay;
import gl2.example.salles.dto.SalleRequest;
import gl2.example.salles.dto.SalleResponse;
import gl2.example.salles.model.Reservation;
import gl2.example.salles.model.Salle;
import gl2.example.salles.repository.SallesRepository;
import gl2.example.salles.service.FileGenerationService;
import gl2.example.salles.service.ReservationService;
import gl2.example.salles.service.SalleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/salles")
public class SalleController {
    @Autowired
    private SalleService salleService;
    @Autowired
    private SallesRepository sallesRepository;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private FileGenerationService fileGenerationService;

    @PostMapping
    public ResponseEntity<SalleResponse> createSalle(@RequestBody SalleRequest salleRequest) {
        SalleResponse salleResponse = new SalleResponse(salleService.createSalle(salleRequest));
        return ResponseEntity.ok(salleResponse);
    }

    @GetMapping
    public ResponseEntity<List<SalleResponse>> findAllSalle() {
        List<Salle> salles = salleService.findAll();
        List<SalleResponse> sallesResponse = salles.stream().map(SalleResponse::new).toList();
        return ResponseEntity.ok(sallesResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalleResponse> findSalleById(@PathVariable Long id) {
        SalleResponse salleResponse = new SalleResponse(salleService.findByID(id));
        return ResponseEntity.ok(salleResponse);
    }

    @GetMapping("/{id}/calendar/{year}/{month}")
    public ResponseEntity<InputStreamResource> downloadCalendarSalle(
            @PathVariable Long id,
            @PathVariable int year,
            @PathVariable int month
    ) {
        YearMonth mois = YearMonth.of(year, month);
        List<CalendarDay> calendrier = fileGenerationService.generateCalendarForSalle(id, mois);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        writer.println("Date, Status, Réserver par");

        for(CalendarDay day : calendrier) {
            writer.printf("%s, %s, %s%n",
                    day.getDate(),
                    day.getStatus(),
                    day.getReservedBy());
        }

        writer.flush();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=calendar_salle_" + id + "_" + month + "_" + year + ".csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(new ByteArrayInputStream(out.toByteArray())));
    }

    @GetMapping("/{id}/calendar")
    public ResponseEntity<List<Map<String, Object>>> getCalendarForSalle(@PathVariable Long id) {
        Optional<Salle> optionalSalle = sallesRepository.findById(id);
        if (optionalSalle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Reservation> reservations = reservationService.findBySalle(optionalSalle.get());

        // Adapter les réservations au format FullCalendar
        List<Map<String, Object>> events = reservations.stream().map(reservation -> {
            Map<String, Object> event = new HashMap<>();
            event.put("title", reservation.getUser().getUsername()); // ou "Réservé"
            event.put("start", reservation.getDateDebut().toString());
            event.put("end", reservation.getDateFin().plusDays(1).toString()); // FullCalendar exclut le dernier jour
            return event;
        }).toList();

        return ResponseEntity.ok(events);
    }

    @GetMapping("/calendar/{id}")
    public ResponseEntity<List<CalendarDay>> getCalendar(@PathVariable Long id) {
        Optional<Salle> optionalSalle = sallesRepository.findById(id);
        if (optionalSalle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Salle salle = optionalSalle.get();
        List<CalendarDay> calendar = reservationService.getCalendarForSalle(salle);
        return ResponseEntity.ok(calendar);
    }

    @GetMapping("/{id}/html_calendar")
    public ModelAndView getHTMLCalendar(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("calendar");
        Salle salle = sallesRepository.findById(id).orElseThrow();

        // Utilisez YearMonth.current() pour le mois actuel ou passez le mois souhaité
        List<CalendarDay> calendar = fileGenerationService.generateCalendarForSalle(id, YearMonth.now());

        modelAndView.addObject("calendarDays", calendar); // Notez le nom "calendarDays" qui correspond au template
        modelAndView.addObject("salle", salle);
        return modelAndView;
    }

    @GetMapping("/{id}/agenda/{year}/{month}")
    public ModelAndView getAgendaView(
            @PathVariable Long id,
            @PathVariable int year,
            @PathVariable int month,
            HttpServletRequest request,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AccessDeniedException("Token manquant ou invalide");
        }

        String token = authHeader.substring(7);

        YearMonth yearMonth = YearMonth.of(year, month);
        Salle salle = sallesRepository.findById(id).orElseThrow();
        List<CalendarDay> calendarDays = fileGenerationService.generateCalendarForSalle(id, yearMonth);

        // Grouper par semaine
        Map<Integer, List<CalendarDay>> weeks = calendarDays.stream()
                .collect(Collectors.groupingBy(
                        day -> day.getDate().get(WeekFields.of(Locale.getDefault()).weekOfMonth())));

        ModelAndView mav = new ModelAndView("agenda");

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            mav.setViewName("agenda :: #calendar-content");
        } else {
            mav.setViewName("agenda");
        }

        mav.addObject("salle", salle);
        mav.addObject("weeks", weeks);
        mav.addObject("yearMonth", yearMonth);
        mav.addObject("previousMonth", yearMonth.minusMonths(1));
        mav.addObject("nextMonth", yearMonth.plusMonths(1));

        return mav;
    }



    @PutMapping("/{id}")
    public ResponseEntity<SalleResponse> updateSalle(@PathVariable Long id, @RequestBody SalleRequest salleRequest) {
        salleService.updateSalle(id, salleRequest);
        return ResponseEntity.ok(new SalleResponse(salleService.findByID(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalle(@PathVariable Long id) {
        salleService.deleteSalle(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/capacite/{capacite}")
    public ResponseEntity<List<Salle>> getSallesByCapacite(@PathVariable Integer capacite) {
        List<Salle> salles = salleService.findByCapacite(capacite);
        if (salles.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si aucune salle n'est trouvée
        }
        return ResponseEntity.ok(salles); // Si des salles sont trouvées
    }
    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<Salle>> getSallesByNom(@PathVariable String nom) {
        List<Salle> salles = salleService.findByNom(nom);
        if (salles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(salles);
    }
}
