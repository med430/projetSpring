package gl2.example.salles.repository;

import gl2.example.salles.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.salle = :salle AND ((r.dateDebut BETWEEN :dateDebut AND :dateFin) OR (r.dateFin BETWEEN :dateDebut AND :dateFin))")
    public List<Reservation> findByIntersectionDatesAndSalle(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin, @Param("salle") Long salleId);
}
