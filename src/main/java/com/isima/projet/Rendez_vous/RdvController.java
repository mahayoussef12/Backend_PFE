package com.isima.projet.Rendez_vous;


import com.isima.projet.Client.ClientRepository;
import com.isima.projet.Entreprise.EntrepriseRepo;
import com.isima.projet.calendrier.domain.Event;
import com.isima.projet.calendrier.repository.EventRepository;
import com.isima.projet.push.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
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
    @GetMapping("/rdv")
    public List<RDV> getAllRDV() {
        return serviceRdv.getAll();
    }

    @GetMapping("/RDV/{id}")
    public RDV getRDVById(@PathVariable Integer id) {
        return serviceRdv.getById(id);
    }
    @PostMapping("/RDV/ajouter/{EntrepriseId}/{ClientId}")

    public Optional<Optional<RDV>> createRDV(@RequestBody RDV rdv, @PathVariable int EntrepriseId , @PathVariable int ClientId ) {
        return  clientRepository.findById((int) Math.toIntExact(ClientId)).map(client -> {
        return repo1.findById((long) Math.toIntExact(EntrepriseId)).map(entreprise -> {
               rdv.setEntreprise(entreprise);
                rdv.setClient(client);
        rdv.setAccepter(Boolean.valueOf("false"));

                return serviceRdv.save(rdv);
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
        er.save(e);
        pushNotificationService.ajouter();
        final RDV updatedEntreprise =repository.save(rdv);
        return ResponseEntity.ok(updatedEntreprise);
    }



/*    @PutMapping("/employees/{id}")
    public ResponseEntity<RDV> updateEmployee(@PathVariable(value = "id") int id,
                                                   @RequestBody RDV employeeDetails) throws ResourceNotFoundException {
        RDV employee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));

        employee.setDate_rdv(employeeDetails.getDate_rdv());

        final RDV updatedEmployee = repository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }*/
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
    @GetMapping("/client/date/{EntrepriseId}")

    public List getRdvDate(@PathVariable Long EntrepriseId) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = dateFormat.parse(String.valueOf(LocalDate.parse(LocalDate.now().toString())));

        List<RDV> rdv = repository.findByEntrepriseId(EntrepriseId);
        List<LocalDateTime> test = new ArrayList<>();

        for (RDV value : rdv)  {
            Date date2 = dateFormat.parse(String.valueOf(value.getDate_rdv()));
            if (date1.equals(date2)) test.add(value.getDate_rdv());
        }
        return test;
    }
    @GetMapping("/client/client/{EntrepriseId}")

    public List<String> getRdvclient(@PathVariable Long EntrepriseId) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse(String.valueOf(LocalDate.parse(LocalDate.now().toString())));

        List<RDV> rdv = repository.findByEntrepriseId(EntrepriseId);
        List<String> test = new ArrayList<>();
        long diff = 0;
        for (RDV value : rdv)  {
            Date date2 = dateFormat.parse(String.valueOf(value.getDate_rdv()));
            if (date1.equals(date2)){

                test.add(value.getClient().getNom()+" "+value.getClient().getPrenom());
            }
        }
        return test;
    }
    @GetMapping("/def/{EntrepriseId}")
    public List defDate(@PathVariable long EntrepriseId) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse(String.valueOf(LocalDate.parse(LocalDate.now().toString())));

        List<RDV> rdv = repository.findByEntrepriseId(EntrepriseId);
        List<String> test = new ArrayList<>();

        for (RDV value : rdv)  {
            Date date2 = dateFormat.parse(String.valueOf(value.getDate_rdv()));
            if (date1.equals(date2)){
                Duration diff = (Duration.between(LocalDateTime.now(),value.getDate_rdv()));
                String hms = String.format("%d:%1d:%1d",
                        diff.toHours(),
                        diff.toMinutesPart(),
                        diff.toSecondsPart());


                test.add(hms);
            }
        }
        return test;
    }
    public Integer hours(LocalDateTime hou) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse(String.valueOf(LocalDate.parse(LocalDate.now().toString())));
        Date date2 = dateFormat.parse(String.valueOf(LocalDate.parse(hou.toString())));
        Integer  diff = Math.toIntExact((Duration.between((Temporal) date1, (Temporal) date2).toHours()));


        return diff;

    }
    @GetMapping("/defmin/{EntrepriseId}")
    public List defDatemin(@PathVariable long EntrepriseId) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse(String.valueOf(LocalDate.parse(LocalDate.now().toString())));

        List<RDV> rdv = repository.findByEntrepriseId(EntrepriseId);
        List<Integer> test = new ArrayList<>();

        for (RDV value : rdv)  {
            LocalDateTime date2 = (value.getDate_rdv());
            if (date1.equals(date2)){
                Integer hour= hours(date2);
                Integer diff = 0;
                long l = hour * 60;
                diff = (int) Duration.between(LocalDateTime.now(),value.getDate_rdv()).toMinutes();
                long min = l - diff;
                test.add(diff);
            }
        }
        return test;
    }
    @GetMapping("list/{EntrepriseId}")
    public List test (@PathVariable long EntrepriseId){
       List<LocalDateTime> test = new ArrayList<>();
       List<RDV> RDV=repository.findByEntrepriseId(EntrepriseId);
        for (RDV value : RDV){
            LocalDateTime date = (value.getDate_rdv());
            test.add(date);
        }
        return test;
    }
}
