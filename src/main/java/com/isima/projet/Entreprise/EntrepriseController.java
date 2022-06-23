package com.isima.projet.Entreprise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isima.projet.Rendez_vous.ResourceNotFoundException;
import com.isima.projet.Super_Admin.Super_admin;
import com.isima.projet.Super_Admin.Super_adminRepository;
import com.isima.projet.push.PushNotificationService;
import com.isima.projet.villedynamique;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
    public static final String ACCOUNT_SID = "AC30d125e7034600076cc44a3c286a0f19";
    public static final String AUTH_TOKEN = "5b3dfa49062cc4b736639b1a6e115bb5";
    private com.isima.projet.Entreprise.Entreprise Entreprise;
    private String success;
    @GetMapping("/entreprise")
    public List<Entreprise> getAllEntreprise() {
        return serviceEntreprise.getAll();
    }
    @GetMapping("/entreprise/{id}")
    public Entreprise getEntrepriseById(@PathVariable long id) {
        return serviceEntreprise.getById(id);  }
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
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            String t="+216"+entreprise1.getTel();
            Message message = Message.creator(
                            new PhoneNumber(t),//The phone number you are sending text to
                            new PhoneNumber("+12342064232"),//The Twilio phone number
                            "Your login verification code is:" + smsCode.getCode()+ "，Valid for 2 minutes")
                    .create();
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
    @GetMapping("/testrandem")
    public List testlist()
    {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(10);
        list.add(11);
        list.add(12);
        list.add(13);
        list.add(14);
        list.add(15);
        list.add(16);
        list.add(17);

        list.add(18);
        list.add(19);
        list.add(20);
        list.add(22);
        list.add(21);
        list.add(23);
        list.add(24);
        List<Integer> listrandem = new ArrayList<>();
        // boundIndex for select in sub list
        Random randomizer = new Random();
        Integer random1 = list.get(randomizer.nextInt(list.size()));
        Integer random2 = list.get(randomizer.nextInt(list.size()));
        Integer random3 = list.get(randomizer.nextInt(list.size()));
        Integer random4= list.get(randomizer.nextInt(list.size()));
        Integer random5 = list.get(randomizer.nextInt(list.size()));
        Integer random6= list.get(randomizer.nextInt(list.size()));
        listrandem.add(random1);
        listrandem.add(random2);
        listrandem.add(random3);
        listrandem.add(random4);
        listrandem.add(random5);
        listrandem.add(random6);
        List<villedynamique> listVille = new ArrayList<>();
        villedynamique object1 = new villedynamique();
        villedynamique object2 = new villedynamique();
        villedynamique object3 = new villedynamique();
        villedynamique object4 = new villedynamique();
        villedynamique object5 = new villedynamique();
        villedynamique object6 = new villedynamique();
        villedynamique object7 = new villedynamique();
        villedynamique object8 = new villedynamique();
        villedynamique object9 = new villedynamique();
        villedynamique object10 = new villedynamique();
        villedynamique object11 = new villedynamique();
        villedynamique object12 = new villedynamique();
        villedynamique object13 = new villedynamique();
        villedynamique object14 = new villedynamique();
        villedynamique object15 = new villedynamique();
        villedynamique object16 = new villedynamique();
        villedynamique object17 = new villedynamique();
        villedynamique object18 = new villedynamique();
        villedynamique object19 = new villedynamique();
        villedynamique object20= new villedynamique();
        villedynamique object24= new villedynamique();
        villedynamique object23 = new villedynamique();
        villedynamique object22 = new villedynamique();
        villedynamique object21= new villedynamique();
        object1.setNomVille("kef");
        object1.setPhoto("assets/le-kef.jpg");
        object2.setNomVille("manouba");
        object2.setPhoto("assets/Manouba.jpg");
        object3.setNomVille("tozeur");
        object3.setPhoto("assets/tozeur.jpg");
        object4.setNomVille("mahdia");
        object4.setPhoto("assets/mahdia.jpg");
        object5.setNomVille("bizerte");
        object5.setPhoto("assets/bizert.jpg");
        object6.setNomVille("zaghouan");
        object6.setPhoto("assets/zaghouan.jpg");
        object7.setNomVille("ariana");
        object7.setPhoto("assets/ariana.jpg");
        object8.setNomVille("ban arous");
        object8.setPhoto("assets/ben-arous.jpg");
        object9.setNomVille("tunis");
        object9.setPhoto("assets/tunis.jpg");
        object10.setNomVille("gabes");
        object10.setPhoto("assets/gabes.jpg");
        object11.setNomVille("sousse");
        object11.setPhoto("assets/sousse.jpg");
        object12.setNomVille("nabeul");
        object12.setPhoto("assets/nabeul.jpg");
        object13.setNomVille("monastir");
        object13.setPhoto("assets/monastir.jpg");
        object14.setNomVille("tataouine");
        object14.setPhoto("assets/tataouine.jpg");
        object15.setNomVille("jendouba");
        object15.setPhoto("assets/jendouba.jpg");
        object16.setNomVille("béja");
        object16.setPhoto("assets/beja.jpg");
        object17.setNomVille("siliana");
        object17.setPhoto("assets/siliana.jpg");
        object18.setNomVille("sfax");
        object18.setPhoto("assets/sfax.jpg");
        object19.setNomVille("medenine");
        object19.setPhoto("assets/medenine.jpg");
        object20.setNomVille("djerba");
        object20.setPhoto("assets/djerba.jpg");
        object21.setNomVille("gafsa");
        object21.setPhoto("assets/gafsa.jpg");
        object22.setNomVille("kairouan");
        object22.setPhoto("assets/kairouan.jpg");
        object23.setNomVille("kasserine");
        object23.setPhoto("/assets/kasserine.jpg");
        object24.setNomVille("kebili");
        object24.setPhoto("/assets/kebili.jpg");
        listVille.add(object3);
        listVille.add(object1);
        listVille.add(object2);
        listVille.add(object4);
        listVille.add(object5);
        listVille.add(object6);
        listVille.add(object7);
        listVille.add(object8);
        listVille.add(object9);
        listVille.add(object10);
        listVille.add(object11);
        listVille.add(object12);
        listVille.add(object13);
        listVille.add(object14);
        listVille.add(object15);
        listVille.add(object16);
        listVille.add(object17);
        listVille.add(object18);
        listVille.add(object19);
        listVille.add(object20);
        listVille.add(object21);
        listVille.add(object22);
        listVille.add(object23);
        listVille.add(object24);

        List<villedynamique> listVillef = new ArrayList<>();
        for (int value : listrandem) {
            listVillef.add(listVille.get(value));
        }
        return listVillef;
    }


       }
