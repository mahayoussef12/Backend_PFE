package com.isima.projet.Entreprise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isima.projet.Rendez_vous.ResourceNotFoundException;
import com.isima.projet.Super_Admin.Super_admin;
import com.isima.projet.Super_Admin.Super_adminRepository;
import com.isima.projet.push.PushNotificationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class EntrepriseController {
	@Autowired
	private EntrepriseRepo repo;
    @Autowired
    public JavaMailSender emailSender;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    Super_adminRepository admin;
    @Autowired
    ServiceEntreprise serviceEntreprise;
    @Autowired
    PushNotificationService pushNotificationService;
@Autowired
EntrepriseRepo repository;

    private com.isima.projet.Entreprise.Entreprise Entreprise;
    private String success;
    @GetMapping("/entreprise")
    public List<Entreprise> getAllEntreprise() {
        return serviceEntreprise.getAll();
    }
    @GetMapping("/entreprise/{id}")
    public Entreprise getEntrepriseById(@PathVariable long id) {

        return serviceEntreprise.getById(id);  }
   // @GetMapping("/entreprise/{nomSociete}")
   /* public Entreprise getEntrepriseByNom(@PathVariable String nomSociete) {
        return serviceEntreprise.getBynom(nomSociete);  }*/
    @GetMapping("/entrepriseEmail/{email}")
    public List<Entreprise> getAllEmail(@PathVariable String email){return  serviceEntreprise.GetEmail(email);}
    @GetMapping("/entreprise/{categorie}/{ville}")
    public  List<Entreprise> GetAllVille(@PathVariable String categorie,@PathVariable String ville){return serviceEntreprise.GetVilleandcategorie(categorie,ville); }
    @GetMapping("/entreprisecategorie/{categorie}")
    public  List<Entreprise> Getcategorie(@PathVariable String categorie){return serviceEntreprise.Getcategorie(categorie); }
    @GetMapping("/entrepriseville/{ville}")
    public  List<Entreprise> Getville(@PathVariable String ville){return serviceEntreprise.GetVille(ville); }
    @GetMapping("/count/{categorie}")
    public  int CountGategorie(@PathVariable String categorie){return repository.countAllByCategorie(categorie); }
    @GetMapping("/countville/{ville}")
    public  int Countville(@PathVariable String ville){return repository.countAllByVille(ville); }
    @PostMapping("verif/{id}")
    public String verif(@PathVariable Long id, @RequestBody String code ) {
        Entreprise entreprise = serviceEntreprise.getById(id);
        if ((code.equals(entreprise.getTest()))&&(LocalDateTime.now().isBefore(entreprise.getTime()))){
            SimpleMailMessage messa = new SimpleMailMessage();
            messa.setTo(entreprise.getEmail());
            messa.setSubject("Confirmation d'inscri");
            messa.setText("vous etes inscrie dans notre platform !! ");
            this.emailSender.send(messa);
            return "true";
        }
        else{
            return"false";
        }
    }
    @GetMapping("/renvoi/code/{identreprise}")
    public Entreprise renvoiCode(@PathVariable long identreprise){
        Entreprise ent=serviceEntreprise.getById(identreprise);
        Smscode smsCode = createSMSCode();
        {
            SimpleMailMessage messa = new SimpleMailMessage();
            messa.setTo(ent.getEmail());
            messa.setSubject("Code");
            messa.setText(smsCode.getCode());
            this.emailSender.send(messa);
            ent.setTest(smsCode.getCode());
            ent.setTime((LocalDateTime) smsCode.getExpireTime());
            return serviceEntreprise.save(ent);
        }}
    @PostMapping("/entreprise/modifier/{id}")
    public String update (@PathVariable(value = "id") long id,@RequestBody String mdp ) throws ResourceNotFoundException {
       Entreprise avis=repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
        avis.setMdp(encoder.encode(String.valueOf(mdp)));
        repository.save(avis);
       return "true";
    }
    public static final String DIRECTORY = "C:/Users/HP/Desktop/pfe/src/assets/img/";
    @PostMapping(value ="/entreprise/ajouter",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public Entreprise createEntreprise(@RequestParam("entreprise") String entreprise,@RequestParam("file") MultipartFile file) throws IOException {
      Entreprise entreprise1 = new ObjectMapper().readValue(entreprise, Entreprise.class);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path fileStorage = get(DIRECTORY, fileName).toAbsolutePath().normalize();
        copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
        String filename = file.getOriginalFilename();
        entreprise1.setImage(fileName);

       entreprise1.setMdp(encoder.encode(entreprise1.getMdp()));

        Smscode smsCode = createSMSCode();
        {
//            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//            String t="+216"+entreprise.getTel();
//
//
//            Message message = Message.creator(
//                            new PhoneNumber(t),//The phone number you are sending text to
//                            new PhoneNumber("+12396030036"),//The Twilio phone number
//                            "Your login verification code is:" + smsCode.getCode()+ "，Valid for 2 minutes")
//                    .create();

        SimpleMailMessage messa = new SimpleMailMessage();
            messa.setTo(entreprise1.getEmail());
            messa.setSubject("Code");
            messa.setText(smsCode.getCode());
            this.emailSender.send(messa);

        }
        entreprise1.setTest(smsCode.getCode());
        entreprise1.setTime((LocalDateTime) smsCode.getExpireTime());
        return serviceEntreprise.save(entreprise1);

    }
    private Smscode createSMSCode() {
        //Introducing commons Lang package
        String code = RandomStringUtils.randomNumeric(5);
        return new Smscode(code, 1800);
    }



    @PutMapping("/employees/{id}")
    public ResponseEntity<Entreprise> updateEntreprise(@PathVariable(value = "id") Long id,
                                              @RequestBody Entreprise newen) throws ResourceNotFoundException {
        Entreprise employee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));

        employee.setBatinda(newen.getBatinda());
        employee.setCreationEntreprise(newen.getCreationEntreprise());
        employee.setNom_gerant(newen.getNom_gerant());
        employee.setTel(newen.getTel());
        employee.setTel_gerant(newen.getTel_gerant());
        employee.setNomSociete(newen.getNomSociete());
        employee.setEmail(newen.getEmail());
        employee.setCategorie(newen.getCategorie());
        employee.setMdp(newen.getMdp());
        employee.setVille(newen.getVille());
        employee.setAdresse(newen.getAdresse());
        employee.setImage(newen.getImage());
        employee.setMat_fiscale(newen.getMat_fiscale());
        final Entreprise updatedEmployee =serviceEntreprise.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }
    @DeleteMapping("/entreprise/delete/{id}")
    public void deleteEntreprise(@PathVariable long id){
       serviceEntreprise.delete(id);
    }
    @PostMapping ("/demande_insc")
    public void demande_insc()
    {
        Super_admin u=admin.test();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(u.getEmail());
        message.setSubject("demande d'inscription");
        message.setText("je veux inscrire dans votre platforme  ");
        this.emailSender.send(message);
    }
}
