package com.isima.projet.Rendez_vous;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceRdv {
    @Autowired
    private RDVRepository repository;
    @Autowired
    RDVRepository repoRDV;
    public List<RDV> getAll()

    {
        List<RDV> rdv = new ArrayList<RDV>();
        repoRDV.findAll().forEach(r -> rdv.add(r));
        return rdv;
    }


    public RDV getById(Integer id)
    {

        return repoRDV.findById(id).get();
    }

    //getting all books record by using the method findaAll() of CrudRepository
    public RDV save(RDV rdv)
    {
        repoRDV.save(rdv); return rdv;
    }
    public void delete(Integer id)
    {
        repoRDV.deleteById(id);
    }
    public void update( RDV rdv,  Integer id)
    {
        repoRDV.save(rdv);
    }
}
