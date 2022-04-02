package com.isima.projet.Facture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceFacture {
    @Autowired
    FactureRepository repof;
    public List<Facture> getAll()

    {
        List<Facture> facture = new ArrayList<Facture>();
        repof.findAll().forEach(fac -> facture.add(fac));
        return facture;
    }


    public Facture getById(Long id)
    {

        return repof.findById(id).get();
    }

    //getting all books record by using the method findaAll() of CrudRepository
    public Facture save(Facture facture)
    {
        repof.save(facture); return facture;
    }
    public void delete( Long id)
    {
        repof.deleteById(id);
    }
    public void update(Facture facture,  Long id)
    {
        repof.save(facture);
    }
}
