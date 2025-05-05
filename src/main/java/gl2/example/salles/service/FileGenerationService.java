package gl2.example.salles.service;

import gl2.example.salles.dto.CalendarDay;
import gl2.example.salles.model.Reservation;
import gl2.example.salles.model.Salle;
import gl2.example.salles.repository.ReservationRepository;
import gl2.example.salles.repository.SallesRepository;
import gl2.example.salles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileGenerationService {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SallesRepository sallesRepository;

    public List<CalendarDay> generateCalendarForSalle(Long salleId, YearMonth mois) {
        Salle salle = sallesRepository.findById(salleId)
                .orElseThrow();

        LocalDate start = mois.atDay(1);
        LocalDate end = mois.atEndOfMonth();

        List<Reservation> reservations = reservationRepository
                .findByIntersectionDatesAndSalle(start, end, salle);

        Map<LocalDate, Reservation> reservationMap = new HashMap<>();
        for (Reservation r : reservations) {
            LocalDate current = r.getDateDebut();
            while (!current.isAfter(r.getDateFin())) {
                reservationMap.put(current, r);
                current = current.plusDays(1);
            }
        }

        List<CalendarDay> calendar = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            Reservation res = reservationMap.get(date);
            calendar.add(new CalendarDay(
                    date,
                    res != null ? "Réservée" : "Disponible",
                    res != null ? res.getUser().getUsername() : ""
            ));
        }

        return calendar;
    }

}
