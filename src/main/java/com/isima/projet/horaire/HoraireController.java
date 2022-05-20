package com.isima.projet.horaire;

import com.isima.projet.Entreprise.EntrepriseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
