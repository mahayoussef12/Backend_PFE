package com.isima.projet.Facture;

import com.google.zxing.WriterException;
import com.isima.projet.QR.QRCodeGenerator;
import com.isima.projet.Service.ServiceRepository;
import com.isima.projet.Service.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
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
    @PostMapping("/ajouter/fac")
    private Facture createfac(@RequestBody Facture fac) throws IOException, WriterException {


        List<service> serv = repos.findAll();

        for (int i = 0; i < serv.size(); i++) {
            fac.setPrix_unitaire_HT(serv.get(i).getPrix_unitaire_HT());

            fac.setTotal_Ht(serv.get(i).getPrix_unitaire_HT()*fac.getQuantite());


            float x = (float) ((fac.getTotal_Ht() * (fac.getTva() / 100)) + fac.getTotal_Ht());
            float a = x - ((x * fac.getRemise()) / 100);
            fac.setTolale_TTC(a);
             QRCodeGenerator.generateQRCodeImage(String.valueOf(fac.getTolale_TTC()),350,350,QR_CODE_IMAGE_PATH);

            fac.setCode("./src/main/resources/QRCodeFacture.png");
        }

        serviceFacture.save(fac);
        return fac;
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
   /*  @PutMapping("/facture/{id}")
    private Facture updateEmployee(@PathVariable Long  id, @RequestBody Facture f)
   {
        Facture fac = repo.findById(id).get();
        fac.setDate_creation(f.getDate_creation());
        fac.setDate_validation(f.getDate_validation());
        fac.setMontant(f.getMontant());
        fac.setRemise(f.getRemise());
        fac.setTva(f.getTva());
        fac.setTtc(f.getTtc());
        fac.setNum_facture(f.getNum_facture());

        return repo.save(fac);
    }*/
}
