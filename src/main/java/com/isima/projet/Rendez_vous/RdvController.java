package com.isima.projet.Rendez_vous;


import com.isima.projet.Client.ClientRepository;
import com.isima.projet.Entreprise.EntrepriseRepo;
import com.isima.projet.Service.ServiceRepository;
import com.isima.projet.Useer;
import com.isima.projet.calendrier.domain.Event;
import com.isima.projet.calendrier.repository.EventRepository;
import com.isima.projet.push.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class RdvController {
    @Autowired
    public JavaMailSender emailSender;
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
    @Autowired
    EntrepriseRepo repo1;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @GetMapping("/rdv")
    public List<RDV> getAllRDV() {
        return serviceRdv.getAll();
    }

    @GetMapping("/RDV/{id}")
    public RDV getRDVById(@PathVariable Integer id) {
        return serviceRdv.getById(id);
    }
    @PostMapping("/RDV/create/{EntrepriseId}/{ClientId}/{ServiceId}")

    public Optional<Optional<Optional<RDV>>> createRDV(@RequestBody RDV rdv, @PathVariable int EntrepriseId , @PathVariable int ClientId, @PathVariable int ServiceId ) {
        return  clientRepository.findById((int) Math.toIntExact(ClientId)).map(client -> {
            return repo1.findById((long) Math.toIntExact(EntrepriseId)).map(entreprise -> {
                return serviceRepository.findById(ServiceId).map(service -> {
                    rdv.setService(service);
                    rdv.setEntreprise(entreprise);
                    rdv.setClient(client);
                    rdv.setAccepter(Boolean.valueOf("false"));
                    SimpleDateFormat date=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    return serviceRdv.save(rdv);
                });


            });
        });
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
    @PutMapping("/acceptation/{id}")
    ResponseEntity<RDV> accepterrdv( @PathVariable int id) throws ResourceNotFoundException {
RDV rdv=repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));

        SimpleMailMessage messa = new SimpleMailMessage();
        messa.setTo(rdv.getClient().getEmail());
        messa.setSubject("Code");
        messa.setText("acceptation rendez vous avec Societe:"+rdv.getEntreprise().getNomSociete()+"De Date Rendez vous :"+rdv.getDate_rdv());
        this.emailSender.send(messa);
        rdv.setAccepter(Boolean.valueOf("true"));
        Event e = new Event();
        e.setStart(rdv.getDate_rdv());
        e.setEnd(rdv.getDate_rdv());
        e.setText("Rendez_vous!");
        e.setEntreprise(rdv.getEntreprise());
        e.setClient(rdv.getClient());
        pushNotificationService.ajouter();
        er.save(e);

        final RDV updatedEntreprise =repository.save(rdv);
        return ResponseEntity.ok(updatedEntreprise);
    }

   @GetMapping("/clients/{EntrepriseId}")
   public ResponseEntity<List<RDV>> getAllRdvEntreprise(@PathVariable(value = "EntrepriseId") Long EntrepriseId) {

       List<RDV> rdv = repository.findByEntrepriseId(EntrepriseId);
       return new ResponseEntity<>(rdv, HttpStatus.OK);
   }
    @GetMapping("/entreprise/nonaccepter/{EntrepriseId}")
    public ResponseEntity<List<RDV>> getAllRdvEntrepriseNo(@PathVariable(value = "EntrepriseId") Long EntrepriseId) {

        List<RDV> rdv = repository.testing(EntrepriseId);
        return new ResponseEntity<>(rdv, HttpStatus.OK);
    }
    @GetMapping("/maha/{clientID}")
    public ResponseEntity<List<RDV>> getAllRdvClient(@PathVariable(value = "clientID") int clientID) {
        List<RDV> rdv = repository.findByClientId(clientID);
        return new ResponseEntity<>(rdv, HttpStatus.OK);
    }
    @GetMapping("/client/nonaccepter/{clientID}")
    public ResponseEntity<List<RDV>> getAllRdvClientNo(@PathVariable(value = "clientID") int clientID) {
        List<RDV> rdv = repository.test(clientID);
        return new ResponseEntity<>(rdv, HttpStatus.OK);
    }
    @DeleteMapping("/RDV/delete/{id}")
    public void deleteRDV(@PathVariable Integer id){
        pushNotificationService.Suppression();
        serviceRdv.delete(id);

    }
    @GetMapping("/client/all/{clientID}")
    public ResponseEntity<List<RDV>> getAllRdv(@PathVariable(value = "clientID") int clientID) {
        List<RDV> rdv = repository.rdvAll(clientID);
        return new ResponseEntity<>(rdv, HttpStatus.OK);
    }

    @GetMapping("/client/rdv/{clientID}")
    public ResponseEntity<List<Useer>> getAllRdvClientrdv(@PathVariable(value = "clientID") int clientID) {
        List<Useer> rdv = repository.getTest(clientID);
        return new ResponseEntity<>(rdv, HttpStatus.OK);
    }
    @GetMapping("/entreprise/rdv/{entrepriseId}")
    public ResponseEntity<List<Useer>> getAllNomClient(@PathVariable(value = "entrepriseId") long entrepriseId) {
        List<Useer> rdv = repository.getNomclient(entrepriseId);
        return new ResponseEntity<>(rdv, HttpStatus.OK);
    }

}
