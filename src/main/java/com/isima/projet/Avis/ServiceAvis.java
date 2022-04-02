package com.isima.projet.Avis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceAvis {
    @Autowired
    AvisRepository repos;
    public List<Avis> getAll()
    {
        List<Avis> avis = new ArrayList<Avis>();
        repos.findAll().forEach(avis1 -> avis.add(avis1));
        return avis;
    }
    public Avis getById(Integer Id)
    {
        return repos.findById(Id).get();
    }
    public Avis save(Avis avis)
    {
        repos.save(avis);
        return avis;
    }
    public void delete(Integer id)
    {
        repos.deleteById(id);
    }


    public void update(Avis avis,  Integer id)
    {
        repos.save(avis);
    }
}
