package com.isima.projet.Entreprise;

import com.google.zxing.WriterException;
import com.isima.projet.QR.QRCodeGenerator;
import com.isima.projet.Super_Admin.Super_admin;
import com.isima.projet.Super_Admin.Super_adminRepository;
import com.isima.projet.push.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";

    private com.isima.projet.Entreprise.Entreprise Entreprise;

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

    @PostMapping("/entreprise/ajouter")
    public Entreprise createEntreprise(@RequestBody Entreprise entreprise) throws IOException, WriterException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(entreprise.getEmail());
        message.setSubject("Confirmation d'inscri");
        message.setText("vous etes inscrie dans notre platform !! ");
        this.emailSender.send(message);
        //entreprise.setMdp(encoder.encode(entreprise.getMdp()));
        String description ="Nom:"+entreprise.getNomSociete()+"Telephone:"+entreprise.getTel()+"Email:"+entreprise.getEmail();
        QRCodeGenerator.generateQRCodeImage(description,350,350,QR_CODE_IMAGE_PATH);
     /* entreprise.setCode("./src/main/resources/QRCode.png");*/
        return serviceEntreprise.save(entreprise);

    }
    @PutMapping("/entreprise/{id}")
    Optional<Entreprise> replaceEntreprise(@RequestBody Entreprise newen, @PathVariable long id) {
        pushNotificationService.update();
        return repository.findById(id)
                .map(employee -> {
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
                    return repository.save(employee);
                });

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
