package gl2.example.salles.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String capacite;

    public Salle() {}

    public Salle(String nom, String capacite) {
        this.nom = nom;
        this.capacite = capacite;
    }
}
