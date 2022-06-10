package com.isima.projet.Facture;

import com.github.royken.converter.FrenchNumberToWords;
import com.google.zxing.WriterException;
import com.isima.projet.Rendez_vous.RDVRepository;
import com.isima.projet.Rendez_vous.ResourceNotFoundException;
import com.isima.projet.Service.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.isima.projet.Facture.status.la_facture_non_payee;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class FactureController {
    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCodeFacture.png";
    @Autowired
    private ServiceFacture serviceFacture;
    @Autowired
    private ServiceRepository repos;
    @Autowired FactureRepository repo;
    @Autowired
    RDVRepository rdvRepository;
    @Autowired
    MessageDigestPasswordEncoder coder;
    private String tst()
    {
        Date date = new Date();
        SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
        String currentYear = getYearFormat.format(date);
        date.getTime();

        return   Integer.toHexString((int) date.getTime())+"_"+currentYear;

    }
    @PostMapping("/ajouter/fac/{id}")
    private Optional<Facture> createfac(@PathVariable long id ) throws IOException, WriterException {
        Facture fac=new Facture();
        return rdvRepository.findById((int) id).map(entreprise -> {
            fac.setRdv(entreprise);
            fac.setPrix_unitaire_HT(fac.getRdv().getService().getPrix_unitaire_HT());
            fac.setTva(19);
            fac.setEtat(la_facture_non_payee);
            fac.setQuantite(1);
            fac.setRemise(5);
            fac.setDescription(fac.getRdv().getService().getDescription());
            fac.setDate_creation(LocalDate.now());
            fac.setTotal_Ht(fac.getRdv().getService().getPrix_unitaire_HT() * fac.getQuantite());
            float x = (float) ((fac.getTotal_Ht() * (fac.getTva() / 100)) + fac.getTotal_Ht());
            float a = x - ((x * fac.getRemise()) / 100);
            fac.setTolale_TTC(a);
            fac.setEntreprise(entreprise.getEntreprise());
            fac.setClient(entreprise.getClient());
            Date date = new Date();
            String ss = "CODY00";
            fac.setNum_facture(ss + (countFacture() + 1));
            fac.setCode(coder.encode(entreprise.getEntreprise().getNomSociete()+fac.getNum_facture()+fac.getTolale_TTC()));
            return serviceFacture.save(fac);

        });
    }


    @GetMapping("/tva/{id}")
    public Float tva(@PathVariable int id)
    {
        Facture fac =serviceFacture.getById((long) id);
        float x = (float) ((fac.getTotal_Ht() * (fac.getTva() / 100)));
        return x;
    }
    @GetMapping(value = "/numbertolettre/{id}", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<String> lettre(@PathVariable long id) {
        serviceFacture.getById(id);

        String xx= converti(serviceFacture.getById(id).getTolale_TTC());
        return new ResponseEntity<String>(xx, HttpStatus.OK);
    }
    public String converti(float number)
    {
        int entier = (int) Math.floor(number);
        int decimal = (int) Math.floor((number - entier) * 100.0f);
        String resultat = FrenchNumberToWords.convert(entier) + ","
                + FrenchNumberToWords.convert(decimal);
        return resultat;
    }
    @GetMapping(value = "/size")
    public int countFacture() {
        return serviceFacture.getAll().size();
    }
    @GetMapping("/factures")
    public List<Facture> getAllFacture() {
        return serviceFacture.getAll();
    }
    @GetMapping("/test/{ClientID}")
    public List<Facture> getFacByNum( @PathVariable Long ClientID){

        List<Facture> facture = repo.findByEntrepriseId(ClientID);
        return facture;
    }
    @GetMapping("/testing/{clID}")
    public List<Facture> getFacByclient( @PathVariable int clID){

        List<Facture> facture = repo.findByClientId(clID);
        return facture;
    }
    @GetMapping("/facture/{id}")
    public Facture getClientById(@PathVariable long id) {
        return serviceFacture.getById(id);
    }
    @PutMapping("/status/{id}")
    public ResponseEntity<Facture> updateStatus(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        Facture employee = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));

        employee.setEtat(status.valueOf("la_facture_payee"));

        final Facture updatedEmployee =repo.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }
    @GetMapping("/facture/code/{code}")
    public Facture facturecode(@PathVariable String code) {
        return repo.findByCode(code);
    }

    @GetMapping("/tva/code/{code}")
    public Float tvaCode(@PathVariable String code)
    {
        Facture fac =repo.findByCode(code);
        float x = (float) ((fac.getTotal_Ht() * (fac.getTva() / 100)));
        return x;
    }
    @GetMapping(value = "/numbertolettre/code/{code}", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<String> lettreCode(@PathVariable String code) {
        repo.findByCode(code);

        String xx= converti(repo.findByCode(code).getTolale_TTC());
        return new ResponseEntity<String>(xx, HttpStatus.OK);
    }
}


