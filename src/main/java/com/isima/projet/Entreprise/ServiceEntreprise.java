package com.isima.projet.Entreprise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceEntreprise {
    @Autowired
    EntrepriseRepo repoE;

    public Entreprise save(Entreprise entreprise) {
        repoE.save(entreprise);

        return entreprise;
    }

    public List<Entreprise> getAll() {
        List<Entreprise> entreprises = new ArrayList<Entreprise>();
        repoE.findAll().forEach(entreprise1 -> entreprises.add(entreprise1));
        return entreprises;
    }

    public Entreprise getById(Long id) {
        return repoE.findById(id).get();
    }


    public void delete(long id) {
        repoE.deleteById(id);
    }

    public Entreprise update(long id, Entreprise entreprise) {
        return entreprise;
    }

    public List<Entreprise> GetEmail(String email) {
        List<Entreprise> entreprises = new ArrayList<Entreprise>();
        repoE.findByEmail(email).forEach(entreprises1 -> entreprises.add(entreprises1));
        return entreprises;
    }



    public List<Entreprise> GetVilleandcategorie(String categorie, String ville) {
        List<Entreprise> entreprises = new ArrayList<Entreprise>();
        repoE.findByCategorieAndVille(categorie,ville).forEach(entreprises1 -> entreprises.add(entreprises1));
        return entreprises;
    }

 /*   public Entreprise getBynom(String nomSociete) {
        return repoE.findByNom(nomSociete);
    }*/
}