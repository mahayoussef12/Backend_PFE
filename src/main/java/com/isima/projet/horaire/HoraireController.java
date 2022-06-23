package com.isima.projet.horaire;

import com.isima.projet.Entreprise.EntrepriseRepo;
import com.isima.projet.Rendez_vous.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class HoraireController {
    @Autowired
    private HoraireRepository repository;

    @Autowired
    EntrepriseRepo repo1;

    @PostMapping("horaire/save/{EntrepriseId}")
    public Optional<horaire> ajouterhoraire(@RequestBody horaire horaire, @PathVariable int EntrepriseId) {
        return repo1.findById((long) Math.toIntExact(EntrepriseId)).map(entreprise -> {
            horaire.setEntreprise(entreprise);
            return repository.save(horaire);
        });
    }
    @GetMapping("horaire/{id}")
    public List<horaire> gethoraie(@PathVariable long id){
        return repository.findByEntrepriseId(id) ;
    }
    @PutMapping("horaire/update/{id}")

    public ResponseEntity<horaire> updateHoraire(@PathVariable(value = "id") Long id,
                                                 @RequestBody horaire newen) throws ResourceNotFoundException {
        horaire horaire = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
      horaire.setDay(newen.getDay());
        horaire.setOpentime(newen.getOpentime());
        horaire.setCloseTime(newen.getCloseTime());
        horaire.setOpentimemidi(newen.getOpentimemidi());
        horaire.setOpentimemidi(newen.getClosetimemidi());
        final horaire updated =repository.save(horaire);
        return ResponseEntity.ok(updated);
    }
}
