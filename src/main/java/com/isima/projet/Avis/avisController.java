package com.isima.projet.Avis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "avis interface", description = "user related crud interface")
@SecurityRequirement(name = "pfe")
public class avisController {
@Autowired
private AvisRepository repository;
    @Autowired
    private ServiceAvis serviceAvis;
    @PostMapping("/avis")
    private ResponseEntity<Avis> create(@RequestBody Avis client){
        Avis res= serviceAvis.save(client);
        return new ResponseEntity<Avis>(res, HttpStatus.CREATED);
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
   /* @PutMapping("/avis/{id}")
    private Avis updateEmployee(@PathVariable Integer id, @RequestBody Avis av)
    {
        Avis avis = avisRepository.findById(id).get();
        avis.setNom_auteur(av.getNom_auteur());
        avis.setDescription(av.getDescription());
        return avisRepository.save(avis);
    }*/
    @DeleteMapping("/avis/{id}")
    private void deleteAvis(@PathVariable("id") Integer id)
    {serviceAvis.delete(id);}

}

