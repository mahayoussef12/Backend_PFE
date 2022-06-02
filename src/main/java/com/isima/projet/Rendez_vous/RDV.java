package com.isima.projet.Rendez_vous;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isima.projet.Client.Client;
import com.isima.projet.Entreprise.Entreprise;
import com.isima.projet.Super_Admin.Super_admin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RDV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_RDV;
    private LocalDateTime date_rdv;
    private  Boolean accepter;
   // private Time horaire;

    @ManyToOne

   /* @JsonIgnore*/
    public Client client;
/*    @JsonProperty("client")
    public int get() {
        return client.getId();
    }*/
    @ManyToOne
/*
    @JsonIgnore*/
    private Entreprise entreprise;
 /*@JsonProperty("entreprise")
    public long geten(){
        return entreprise.getId();
    }*/

    public Entreprise getEntreprise() {
        return entreprise;
    }

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "super_admin_id_admin")
    private Super_admin super_admin;

    public Super_admin getSuper_admin() {
        return super_admin;
    }

    public void setSuper_admin(Super_admin super_admin) {
        this.super_admin = super_admin;
    }
@JsonIgnore
    public void setEntreprise(Entreprise entreprise) {
        this.entreprise=entreprise;
    }
    @JsonIgnore
    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
    @JsonIgnore
    public com.isima.projet.Service.service getService() {
        return service;
    }
    @JsonIgnore
    public void setService(com.isima.projet.Service.service service) {
        this.service = service;
    }

    @OneToOne
    public com.isima.projet.Service.service service;
}
