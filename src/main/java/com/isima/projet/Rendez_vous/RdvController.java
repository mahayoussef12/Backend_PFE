package com.isima.projet.Rendez_vous;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RdvController {
    public static final String ACCOUNT_SID = "AC4ef54b6c78aceb1e328e5e753de97c45";
    public static final String AUTH_TOKEN = "fc77906e886a27514d7ac38c5e6fbff8";
    @Autowired
   private ServiceRdv serviceRdv;
    @Autowired
    private RDVRepository repoR;
    private RDV r;

    @GetMapping("/rdv")
    public List<RDV> getAllRDV() {
        return serviceRdv.getAll();
    }

    @GetMapping("/RDV/{id}")
    public RDV getRDVById(@PathVariable Integer id) {
        return serviceRdv.getById(id);
    }
    @PostMapping("/RDV/ajouter")
    public RDV createRDV(@RequestBody RDV rdv) {
        return serviceRdv.save(rdv);
    }

   /* @PutMapping("/RDV/{id}")
    public RDV updateRDV(@PathVariable Integer id, @RequestBody RDV rdv)  {
        RDV rdv1=repo.findById(id).get();
        rdv1.setId_RDV(rdv.getId_RDV());
        rdv1.setDate_rdv(rdv.getDate_rdv());
        //rdv1.setHoraire(rdv.getHoraire());
        return repo.save(rdv);
    }*/
   @GetMapping("/rdv/{id_client}")
   public RDV getRDVByIdClient(@PathVariable Integer id_client) {
       List<RDV> rd = repoR.findAll();
       for (int i = 0; i < rd.size(); i++) {
           Integer id= (rd.get(i).getClient().getId());
           if (id_client==id) {
               RDV  r = repoR.findById(id).get();

           }
       }

       return r;

   }
    @DeleteMapping("/RDV/delete/{id}")
    public void deleteRDV(@PathVariable Integer id){
        serviceRdv.delete(id);
    }

}