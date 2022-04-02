package com.isima.projet.Facture;

import com.google.zxing.WriterException;
import com.isima.projet.QR.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FactureController {
    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCodeFacture.png";
    @Autowired
    private ServiceFacture serviceFacture;

    @PostMapping("/ajouter/fac")
    private String createfac(@RequestBody Facture fac) throws IOException, WriterException {

         QRCodeGenerator.generateQRCodeImage(String.valueOf(fac.getEtat()),350,350,QR_CODE_IMAGE_PATH);

        fac.setCode("./src/main/resources/QRCodeFacture.png");
        serviceFacture.save(fac);
        return "Facture creer avec succes !! vous permet de Coonsulter le Code Qr ";
    }

    @GetMapping("/factures")
    public List<Facture> getAllFacture() {
        return serviceFacture.getAll();
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
