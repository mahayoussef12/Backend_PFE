package com.isima.projet.Facture;

import com.isima.projet.Client.Client;
import com.isima.projet.Entreprise.Entreprise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id_facture;
    private Date date_creation;
    private Date date_validation;
    private float prix_unitaire_HT;

    private status etat;
    private String code;
    private int num_facture ;
    private int quantite;

    private float total_Ht;
    private float tva;
    private int remise;
    private float tolale_TTC;

    @ManyToOne
    private Client client;
    @ManyToOne
    private Entreprise entreprise;

/*
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "super_admin_id_admin")
    private Super_admin super_admin;

    public Super_admin getSuper_admin() {
        return super_admin;
    }

    public void setSuper_admin(Super_admin super_admin) {
        this.super_admin = super_admin;
    }*/
}
