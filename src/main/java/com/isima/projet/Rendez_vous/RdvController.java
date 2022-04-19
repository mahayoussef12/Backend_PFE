package com.isima.projet.Rendez_vous;


import com.isima.projet.calendrier.domain.Event;
import com.isima.projet.calendrier.repository.EventRepository;
import com.isima.projet.push.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class RdvController {
    public static final String ACCOUNT_SID = "AC4ef54b6c78aceb1e328e5e753de97c45";
    public static final String AUTH_TOKEN = "fc77906e886a27514d7ac38c5e6fbff8";
    @Autowired
   private ServiceRdv serviceRdv;
    @Autowired
    private RDVRepository repository;
    private RDV r;
    @Autowired
    PushNotificationService pushNotificationService;
    @Autowired
    EventRepository er;
    @GetMapping("/rdv")
    public List<RDV> getAllRDV() {
        return serviceRdv.getAll();
    }

    @GetMapping("/RDV/{id}")
    public RDV getRDVById(@PathVariable Integer id) {
        return serviceRdv.getById(id);
    }
    @PostMapping("/RDV/ajouter")
    public RDV createRDV(@RequestBody RDV rdv) {
        Event e = new Event();
        e.setStart(rdv.getDate_rdv());
        e.setEnd(rdv.getDate_rdv());
        e.setText("Rendez_vous!");
        er.save(e);
        pushNotificationService.ajouter();
        return serviceRdv.save(rdv);

    }
    @PutMapping("/rdv/{id}")
    Optional<RDV> replaceEmployee(@RequestBody RDV newrdv, @PathVariable int id) {
        pushNotificationService.update();
        return repository.findById(id)
                .map(employee -> {
                    employee.setDate_rdv(newrdv.getDate_rdv());

                    return repository.save(employee);
                });

    }
   @GetMapping("/clients/{EntrepriseId}")
   public ResponseEntity<List<RDV>> getAllRdvEntreprise(@PathVariable(value = "EntrepriseId") Long EntrepriseId) {

       List<RDV> rdv = repository.findByEntrepriseId(EntrepriseId);
       return new ResponseEntity<>(rdv, HttpStatus.OK);
   }

    @GetMapping("/maha/{clientID}")
    public ResponseEntity<List<RDV>> getAllRdvClient(@PathVariable(value = "clientID") int clientID) {

        List<RDV> rdv = repository.findByClientId(clientID);
        for (int i = 0; i < rdv.size(); i++) {
        Event e = new Event();
        e.setStart(rdv.get(i).getDate_rdv());
        e.setEnd(rdv.get(i).getDate_rdv());
        e.setText("test");
        er.save(e);}
        return new ResponseEntity<>(rdv, HttpStatus.OK);
    }
    @DeleteMapping("/RDV/delete/{id}")
    public void deleteRDV(@PathVariable Integer id){
        pushNotificationService.Suppression();
        serviceRdv.delete(id);

    }
    @GetMapping("/count/{clientid}")
    public Long testcount(@PathVariable int clientid){
        Long n=repository.countByClientId(clientid);
        return n;
    }
}
