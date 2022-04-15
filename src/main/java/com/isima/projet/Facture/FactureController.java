package com.isima.projet.Facture;

import com.google.zxing.WriterException;
import com.isima.projet.QR.QRCodeGenerator;
import com.isima.projet.Service.ServiceRepository;
import com.isima.projet.Service.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

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

        byte[] image = new byte[0];
        List<service> serv = repos.findAll();

        for (int i = 0; i < serv.size(); i++) {
            fac.setPrix_unitaire_HT(serv.get(i).getPrix_unitaire_HT());

            fac.setTotal_Ht(serv.get(i).getPrix_unitaire_HT()*fac.getQuantite());


            float x = (float) ((fac.getTotal_Ht() * (fac.getTva() / 100)) + fac.getTotal_Ht());
            float a = x - ((x * fac.getRemise()) / 100);
            fac.setTolale_TTC(a);
             QRCodeGenerator.generateQRCodeImage(String.valueOf(fac.getTolale_TTC()),350,350,QR_CODE_IMAGE_PATH);
            //QRCodeGenerator.getQRCodeImage("hello", 350, 350);
            //String qrcode = Base64.getEncoder().encodeToString(image);

           // fac.setCode(qrcode);
           /* loadFile(fac.getCode());*/

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



    private final Path rootLocation = Paths.get("upload-dir");


    public void store(MultipartFile file) {
        try {
            Path Pathfile = this.rootLocation.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            Resource resource = new UrlResource(Pathfile.toUri());
            if (!resource.exists()) {
                Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
            }
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }

    public Resource loadFile(String filename) {
        try {
            Path file = this.rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }
}

