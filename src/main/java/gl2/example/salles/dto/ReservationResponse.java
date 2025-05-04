package gl2.example.salles.dto;

import gl2.example.salles.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private UserResponse user;
    private SalleResponse salle;

    public ReservationResponse(Reservation reservation) {
        id = reservation.getId();
        dateDebut = reservation.getDateDebut();
        dateFin = reservation.getDateFin();
        user = new UserResponse(reservation.getUser());
        salle = new SalleResponse(reservation.getSalle());
    }
}
