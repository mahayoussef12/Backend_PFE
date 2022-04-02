package com.isima.projet.Facture;

import com.isima.projet.Client.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_facture;
    private Date date_creation;
    private Date date_validation;
    private float tva;
    private float ttc;
    private float remise;
    private float montant;
    private status etat;
    private String code;
    private int num_facture ;
    @OneToMany(mappedBy = "facture")
    private Collection<Client>client;

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
