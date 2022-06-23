package com.isima.projet.Service;

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
public class ServiceController {
    @Autowired
    private Serviceservice serviceservice;
    @Autowired
    ServiceRepository reposer;
    @Autowired
    EntrepriseRepo repo1;
    @Autowired
    ServiceRepository repo;

    @GetMapping("/service")
    public List<service> getAllService() {
        return serviceservice.getAll();
    }

    @GetMapping("/service/{id}")
    public service getServiceById(@PathVariable Integer id) {
        return serviceservice.getById(id);
    }

    @PostMapping("/ajouter")

    public service createService(@RequestBody service ser) {

        return serviceservice.save(ser);
    }

    /*    @PutMapping("service/{id}")
        public  service updateService(@PathVariable Integer id, @RequestBody service ser)  {
            service ser1=repo.findById(id).get();
            ser1.setLib_service(ser.getLib_service());
            ser1.setCout(ser.getCout());
            ser1.setDescription(ser.getDescription());
            ser1.setTax(ser.getTax());
            ser1.setType(ser.getType());
            return repo.save(ser);
        }*/
    @PutMapping("service/update/{id}")

    public ResponseEntity<service> updateService(@PathVariable(value = "id") Integer id,
                                                       @RequestBody service newen) throws ResourceNotFoundException {
        service service = reposer.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));

        service.setLib_service(newen.getLib_service());
        service.setDescription(newen.getDescription());
        service.setPrix_unitaire_HT(newen.getPrix_unitaire_HT());
        final service updated =reposer.save(service);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/service/delete/{id}")
    public void deleteService(@PathVariable Integer id) {
        serviceservice.delete(id);
    }



    @PostMapping("service/save/{EntrepriseId}")
    private Optional<service> ajouterservice(@RequestBody service ser, @PathVariable int EntrepriseId) {
        return repo1.findById((long) Math.toIntExact(EntrepriseId)).map(entreprise -> {
            ser.setAfficher(Boolean.valueOf("true"));
            ser.setEntreprise(entreprise);
            return reposer.save(ser);
        });
    }

    @PutMapping("service/desactiver/{id}")
    public ResponseEntity<service> desactiver(@PathVariable Integer id) throws ResourceNotFoundException {
     service avis=reposer.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
        avis.setAfficher(Boolean.valueOf("False"));
        final service updatedEntreprise =reposer.save(avis);
        return ResponseEntity.ok(updatedEntreprise);

    }

    @PutMapping("service/Activer/{id}")
    public ResponseEntity<service> Activee(@PathVariable Integer id) throws ResourceNotFoundException {
        service avis=reposer.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
        avis.setAfficher(Boolean.valueOf("True"));
        final service updatedEntreprise =reposer.save(avis);
        return ResponseEntity.ok(updatedEntreprise);

    }

    @GetMapping("/servise/entreprise/{id}")
    public List<service> listService(@PathVariable long id) {
        return repo.findByEntrepriseId(id);
    }
    @GetMapping("/service/entreprise/desactive/{id}")
    public List<service> listServicedesactivee(@PathVariable long id) {
        return repo.findByEntrepriseIddsc(id);
    }
}
