package gl2.example.salles.controller;

import gl2.example.salles.dto.SalleRequest;
import gl2.example.salles.dto.SalleResponse;
import gl2.example.salles.model.Salle;
import gl2.example.salles.service.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salles")
public class SalleController {
    @Autowired
    private SalleService salleService;

    @PostMapping
    public ResponseEntity<SalleResponse> createSalle(@RequestBody SalleRequest salleRequest) {
        SalleResponse salleResponse = new SalleResponse(salleService.createSalle(salleRequest));
        return ResponseEntity.ok(salleResponse);
    }

    @GetMapping
    public ResponseEntity<List<SalleResponse>> findAllSalle() {
        List<Salle> salles = salleService.findAll();
        List<SalleResponse> sallesResponse = salles.stream().map(SalleResponse::new).toList();
        return ResponseEntity.ok(sallesResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalleResponse> findSalleById(@PathVariable Long id) {
        SalleResponse salleResponse = new SalleResponse(salleService.findByID(id));
        return ResponseEntity.ok(salleResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalleResponse> updateSalle(@PathVariable Long id, @RequestBody SalleRequest salleRequest) {
        salleService.updateSalle(id, salleRequest);
        return ResponseEntity.ok(new SalleResponse(salleService.findByID(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalle(@PathVariable Long id) {
        salleService.deleteSalle(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/capacite/{capacite}")
    public ResponseEntity<List<Salle>> getSallesByCapacite(@PathVariable Integer capacite) {
        List<Salle> salles = salleService.findByCapacite(capacite);
        if (salles.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si aucune salle n'est trouvée
        }
        return ResponseEntity.ok(salles); // Si des salles sont trouvées
    }
    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<Salle>> getSallesByNom(@PathVariable String nom) {
        List<Salle> salles = salleService.findByNom(nom);
        if (salles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(salles);
    }
}
