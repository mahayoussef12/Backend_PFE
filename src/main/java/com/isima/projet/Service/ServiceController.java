package com.isima.projet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ServiceController {
    @Autowired
    private Serviceservice serviceservice;
    @GetMapping("/service")
    public List<service> getAllService(){
        return serviceservice.getAll() ;
    }
    @GetMapping("/service/{id}")
    public service getServiceById(@PathVariable Integer id)
    {
        return serviceservice.getById(id);
    }
    @PostMapping("/ajouter")

    public service createService( @RequestBody  service ser)
    {
        return serviceservice.save(ser);
    }
/*    @PutMapping("service/{id}")
    public  service updateService(@PathVariable Integer id, @RequestBody service ser)  {
        service ser1=repo.findById(id).get();
        ser1.setLib_service(ser.getLib_service());
        ser1.setCout(ser.getCout());
        ser1.setDescription(ser.getDescription());
        ser1.setTax(ser.getTax());
        ser1.setType(ser.getType());
        return repo.save(ser);
    }*/

    @DeleteMapping("/service/delete/{id}")
    public void deleteService(@PathVariable Integer id){
    serviceservice.delete(id);
    }
}
