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
                        .salles(salle)
                        .build();
        reservationRepository.save(reservation);
        return reservation;
    }
}
