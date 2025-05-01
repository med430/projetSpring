package gl2.example.salles.dto;

import gl2.example.salles.model.Salle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalleResponse {
    private String nom;
    private int capacite;

    public SalleResponse(Salle salle) {
        this.nom = salle.getNom();
        this.capacite = salle.getCapacite();
    }
}
