package gl2.example.salles.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Salles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String capacite;

    public Salles() {}

    public Salles(String nom, String capacite) {
        this.nom = nom;
        this.capacite = capacite;
    }
}
