package com.isima.projet.Facture;

import com.isima.projet.Client.Client;
import com.isima.projet.Entreprise.Entreprise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_facture;
    private LocalDate date_creation;
    private Date date_validation;
    private float prix_unitaire_HT;

    private status etat;
    private String code;
    private  String num_facture ;
    private int quantite;

    private float total_Ht;
    private float tva;
    private int remise;
    private float tolale_TTC;
    private String description ;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client client;
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Entreprise entreprise;


 /*   @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "super_admin_id_admin")
    private Super_admin super_admin;

    public Super_admin getSuper_admin() {
        return super_admin;
    }

    public void setSuper_admin(Super_admin super_admin) {
        this.super_admin = super_admin;
    }*/
}
