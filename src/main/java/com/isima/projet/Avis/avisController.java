package com.isima.projet.Avis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")

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
    private Avis create(@RequestBody Avis client){

       return serviceAvis.save(client);

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
        return repository.testing(id);
    }

}

