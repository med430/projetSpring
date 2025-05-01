package gl2.example.salles.repository;

import gl2.example.salles.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SallesRepository extends JpaRepository<Salle, Long> {
    public Salle findByNom(String nom);
    public Salle findByCapacite(int capacite);
}
