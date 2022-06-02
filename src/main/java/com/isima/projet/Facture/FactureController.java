package com.isima.projet.Facture;

import com.github.royken.converter.FrenchNumberToWords;
import com.google.zxing.WriterException;
import com.isima.projet.Service.ServiceRepository;
import com.isima.projet.Service.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class FactureController {
    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCodeFacture.png";
    @Autowired
    private ServiceFacture serviceFacture;
    @Autowired
   ServiceRepository repos;
    @Autowired FactureRepository repo;
    private String tst()
    {
        Date date = new Date();
        SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
        String currentYear = getYearFormat.format(date);
        date.getTime();

        return   Integer.toHexString((int) date.getTime())+"_"+currentYear;

    }
    @PostMapping("/ajouter/fac")
    private Facture createfac(@RequestBody Facture fac) throws IOException, WriterException {

        byte[] image = new byte[0];
        List<service> serv = repos.findAll();

        for (int i = 0; i < serv.size(); i++) {
            fac.setPrix_unitaire_HT(serv.get(i).getPrix_unitaire_HT());
            fac.setTva(19);
            fac.setDescription(serv.get(i).getDescription());

            fac.setDate_creation(LocalDate.now());

            fac.setTotal_Ht(serv.get(i).getPrix_unitaire_HT()*fac.getQuantite());
            float x = (float) ((fac.getTotal_Ht() * (fac.getTva() / 100)) + fac.getTotal_Ht());
            float a = x - ((x * fac.getRemise()) / 100);
            fac.setTolale_TTC(a);
            Date date = new Date();
            SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
            String currentYear = getYearFormat.format(date);
            date.getTime();
            String ss=tst();
            fac.setNum_facture(ss);


        }

        serviceFacture.save(fac);
        return fac;
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


}


