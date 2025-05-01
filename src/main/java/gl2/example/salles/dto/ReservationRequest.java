package gl2.example.salles.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private UserRequest user;
    private SalleRequest salle;
}
