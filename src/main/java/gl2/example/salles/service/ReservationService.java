package gl2.example.salles.service;

import gl2.example.salles.dto.CalendarDay;
import gl2.example.salles.dto.ReservationRequest;
import gl2.example.salles.model.Reservation;
import gl2.example.salles.model.Salle;
import gl2.example.salles.model.User;
import gl2.example.salles.repository.ReservationRepository;
import gl2.example.salles.repository.SallesRepository;
import gl2.example.salles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private SallesRepository sallesRepository;
    @Autowired
    private UserRepository userRepository;

    public Reservation createReservation(ReservationRequest reservationRequest) {
        User user = userRepository.findByUsernameOrEmail(reservationRequest.getUser().getUsername(), reservationRequest.getUser().getEmail())
                .orElseThrow();
        if(!IsCurrentUser(user)) {
            throw new RuntimeException();
        }
        Salle salle = sallesRepository.findById(reservationRequest.getSalle().getId())
                .orElseThrow();
        Reservation reservation = Reservation.builder()
                        .dateDebut(reservationRequest.getDateDebut())
                        .dateFin(reservationRequest.getDateFin())
                        .user(user)
                        .salle(salle)
                        .build();

        if(checkIntersectionDates(reservation.getDateDebut(), reservation.getDateFin(), salle)) {
            return null;
        }

        reservationRepository.save(reservation);
        return reservation;
    }

    public Reservation findByID(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow();
        return reservation;
    }

    public List<Reservation> findByUser(User user) {
        List<Reservation> reservations = reservationRepository.findAll()
                .stream().filter(reservation -> reservation.getUser().equals(user)).toList();
        return reservations;
    }

    public List<Reservation> findBySalle(Salle salle) {
        List<Reservation> reservations = reservationRepository.findAll()
                .stream().filter(reservation -> reservation.getSalle().equals(salle)).toList();
        return reservations;
    }

    public List<Reservation> findByDateDebut(String dateDebut) {
        List<Reservation> reservations = reservationRepository.findAll()
                .stream().filter(reservation -> reservation.getDateDebut().equals(dateDebut)).toList();
        return reservations;
    }

    public List<Reservation> findByDateFin(String dateFin) {
        List<Reservation> reservations = reservationRepository.findAll()
                .stream().filter(reservation -> reservation.getDateFin().equals(dateFin)).toList();
        return reservations;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).get();
        User user = reservation.getUser();
        if(!IsCurrentUser(user)) {
            throw new RuntimeException();
        }
        reservationRepository.deleteById(id);
    }

    public void updateReservation(Long id, ReservationRequest reservationRequest) {
        User user = userRepository.findByUsername(reservationRequest.getUser().getUsername())
                .orElseThrow();
        if(!IsCurrentUser(user)) {
            throw new RuntimeException();
        }
        Salle salle = sallesRepository.findById(reservationRequest.getSalle().getId())
                .orElseThrow();
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow();
        if(!IsCurrentUser(reservation.getUser())) {
            throw new RuntimeException();
        }
        reservation.setDateDebut(reservationRequest.getDateDebut());
        reservation.setDateFin(reservationRequest.getDateFin());
        reservation.setUser(user);
        reservation.setSalle(salle);
        if(checkIntersectionDates(reservation.getDateDebut(), reservation.getDateFin(), salle)) {
            return;
        }
        reservationRepository.save(reservation);
    }

    public List<CalendarDay> getCalendarForSalle(Salle salle) {
        List<Reservation> reservations = findBySalle(salle);
        Map<LocalDate, String> reservedDates = new HashMap<>();

        for (Reservation res : reservations) {
            LocalDate d = res.getDateDebut();
            while (!d.isAfter(res.getDateFin())) {
                reservedDates.put(d, res.getUser().getUsername());
                d = d.plusDays(1);
            }
        }

        LocalDate start = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate end = start.plusYears(1).minusDays(1);
        List<CalendarDay> calendar = new ArrayList<>();

        while (!start.isAfter(end)) {
            String reservedBy = reservedDates.get(start);
            String status = reservedBy != null ? "Réservée" : "Disponible";


            calendar.add(CalendarDay.builder()
                    .date(start)
                    .status(status)
                    .reservedBy(reservedBy)
                    .build());

            start = start.plusDays(1);
        }

        return calendar;
    }



    private boolean IsCurrentUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User current_user = userRepository.findByUsername(username).get();
        return current_user.equals(user);
    }

    private boolean checkIntersectionDates(LocalDate dateDebut, LocalDate dateFin, Salle salle){
        List<Reservation> r = reservationRepository.findByIntersectionDatesAndSalle(dateDebut, dateFin, salle);
        return r.size() > 0;
    }
}
