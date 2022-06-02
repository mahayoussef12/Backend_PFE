package com.isima.projet.Service;

import com.isima.projet.Entreprise.EntrepriseRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Optional<service> updateService(@PathVariable Integer id, @RequestBody service ser) {
        return reposer.findById(id)
                .map(employee -> {
                    employee.setLib_service(ser.getLib_service());
                    employee.setDescription(ser.getLib_service());
                    employee.setPrix_unitaire_HT(ser.getPrix_unitaire_HT());
                    return reposer.save(employee);
                });

    }


    @DeleteMapping("/service/delete/{id}")
    public void deleteService(@PathVariable Integer id) {
        serviceservice.delete(id);
    }



    @PostMapping("service/save/{EntrepriseId}")
    private Optional<service> ajouterservice(@RequestBody service ser, @PathVariable int EntrepriseId) {
        return repo1.findById((long) Math.toIntExact(EntrepriseId)).map(entreprise -> {
            ser.setEntreprise(entreprise);
            return reposer.save(ser);
        });
    }




    @GetMapping("/servise/entreprise/{id}")
    public List<service> listService(@PathVariable long id) {
        return repo.findByEntrepriseId(id);
    }
}
