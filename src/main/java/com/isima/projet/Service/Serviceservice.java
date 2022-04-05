package com.isima.projet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Serviceservice {
    @Autowired
    ServiceRepository repoS;
    public List<service> getAll()

    {
        List<service> ser = new ArrayList<service>();
        repoS.findAll().forEach(s -> ser.add(s));
        return ser;
    }


    public service getById(Integer id)
    {

        return repoS.findById(id).get();
    }

    //getting all books record by using the method findaAll() of CrudRepository
    public service save(service ser)
    {
        repoS.save(ser); return ser;
    }
    public void delete(Integer id)
    {
        repoS.deleteById(id);
    }

    public void update( service ser,  Integer id)
    {
        service service = repoS.findById(id).get();

        service.setLib_service(ser.getLib_service());
        service.setDescription(ser.getDescription());
        service.setType(ser.getType());
        service.setPrix_unitaire_HT(ser.getPrix_unitaire_HT());


        repoS.save(service);

    }

}
