package gl2.example.salles.service;

import gl2.example.salles.dto.SalleRequest;
import gl2.example.salles.model.Salle;
import gl2.example.salles.repository.SallesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalleService {
    @Autowired
    private SallesRepository sallesRepository;

    public Salle createSalle(SalleRequest salleRequest){
        Salle salle = new Salle();
        salle.setNom(salleRequest.getNom());
        salle.setCapacite(salleRequest.getCapacite());
        sallesRepository.save(salle);
        return salle;
    }

    public Salle findByID(Long id){
        Salle salle = sallesRepository.findById(id)
                .orElseThrow();
        return salle;
    }

    public List<Salle> findByNom(String nom){
        List<Salle> salles = sallesRepository.findAll()
                .stream().filter(salle -> salle.getNom().equals(nom)).toList();
        return salles;
    }

    public List<Salle> findAll(){
        return sallesRepository.findAll();
    }

    public void deleteSalle(Long id){
        sallesRepository.deleteById(id);
    }

    public void updateSalle(Long id, SalleRequest salleRequest){
        Salle salle = sallesRepository.findById(id)
                .orElseThrow();
        salle.setNom(salleRequest.getNom());
        salle.setCapacite(salleRequest.getCapacite());
        sallesRepository.save(salle);
    }
}
