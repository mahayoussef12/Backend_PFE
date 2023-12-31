package com.isima.projet.Avis;

import com.isima.projet.Entreprise.EntrepriseRepo;
import com.isima.projet.Rendez_vous.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.lang.Math.round;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/v1")
@Tag(name = "avis interface", description = "user related crud interface")
@SecurityRequirement(name = "pfe")
public class avisController {
@Autowired
private AvisRepository repository;
@Autowired
private EntrepriseRepo repo;
    @Autowired
    private ServiceAvis serviceAvis;

    @PostMapping("/create/{EntrepriseId}")
    Avis createAvis(@PathVariable (value = "EntrepriseId") Long EntrepriseId,
                          @Valid @RequestBody Avis avis ) {
        return repo.findById((long) Math.toIntExact(EntrepriseId)).map(entreprise -> {
            avis.setEntreprise(entreprise);
            avis.setAfficher(Boolean.valueOf("False"));
            return repository.save(avis);
        }).orElseThrow(() -> new IllegalArgumentException("EntrepriseId " + EntrepriseId + " not found"));
    }



    @Operation(summary = "get user list data")
    @GetMapping("/avis")
    private List<Avis> getAllAvis()
    {
        return serviceAvis.getAll();
    }
    @GetMapping("/avis/{id}")
    private Avis getAllAvisById(@PathVariable Integer id)
    {
        return serviceAvis.getById(id);
    }

    @DeleteMapping("/avis/{id}")
    private void deleteAvis(@PathVariable("id") Integer id)
    {serviceAvis.delete(id);}

   @GetMapping("/countavis/{id}")
    public double count(@PathVariable long id){
        return round (repository.testing(id));
    }
    @GetMapping("/aviss/{id}")
    public List<Avis> getEntrepriseId (@PathVariable long id ){
        return  repository.test(id);
    }
    @GetMapping("/avisNonActiver/{id}")
    public List<Avis> getEntrepriseIdNonActive (@PathVariable long id ){
        return  repository.Non_Active(id);
    }

    @GetMapping("/nbAvis/{id}")
    public double countAvisEntrepriseId (@PathVariable long id ){
        return  repository.count(id);
    }
    @PutMapping("/avis/modifier/{id}")
    public ResponseEntity<Avis> update (@PathVariable(value = "id") int id) throws ResourceNotFoundException {
        Avis avis=repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
        avis.setAfficher(Boolean.valueOf("True"));
        final Avis updatedEntreprise =repository.save(avis);
        return ResponseEntity.ok(updatedEntreprise);
    }
    @PutMapping("/avis/desactiver/{id}")
    public ResponseEntity<Avis> updateDesactive (@PathVariable(value = "id") int id) throws ResourceNotFoundException {
        Avis avis=repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
        avis.setAfficher(Boolean.valueOf("False"));
        final Avis updatedEntreprise =repository.save(avis);
        return ResponseEntity.ok(updatedEntreprise);
    }
}

