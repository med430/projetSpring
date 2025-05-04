package gl2.example.salles.service;

import gl2.example.salles.dto.ReservationRequest;
import gl2.example.salles.model.Reservation;
import gl2.example.salles.model.Salle;
import gl2.example.salles.model.User;
import gl2.example.salles.repository.ReservationRepository;
import gl2.example.salles.repository.SallesRepository;
import gl2.example.salles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private SallesRepository sallesRepository;
    @Autowired
    private UserRepository userRepository;

    public Reservation createReservation(ReservationRequest reservationRequest) {
        User user = userRepository.findByUsername(reservationRequest.getUser().getUsername())
                .orElseThrow();
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
        reservationRepository.deleteById(id);
    }

    public void updateReservation(Long id, ReservationRequest reservationRequest) {
        User user = userRepository.findByUsername(reservationRequest.getUser().getUsername())
                .orElseThrow();
        Salle salle = sallesRepository.findById(reservationRequest.getSalle().getId())
                .orElseThrow();
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow();
        reservation.setDateDebut(reservationRequest.getDateDebut());
        reservation.setDateFin(reservationRequest.getDateFin());
        reservation.setUser(user);
        reservation.setSalle(salle);
        if(checkIntersectionDates(reservation.getDateDebut(), reservation.getDateFin(), salle)) {
            return;
        }
        reservationRepository.save(reservation);
    }

    private boolean checkIntersectionDates(LocalDate dateDebut, LocalDate dateFin, Salle salle){
        List<Reservation> r = reservationRepository.findByIntersectionDatesAndSalle(dateDebut, dateFin, salle);
        return r.size() > 0;
    }
}
