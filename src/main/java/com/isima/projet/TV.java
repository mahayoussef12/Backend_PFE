package com.isima.projet;

import java.time.LocalDateTime;

public class TV {
    private String nom ;
    private LocalDateTime date_rdv;
    private Long  temps_restant;
    private boolean visible;
    private String service;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public LocalDateTime getDate_rdv() {
        return date_rdv;
    }

    public void setDate_rdv(LocalDateTime date_rdv) {
        this.date_rdv = date_rdv;
    }

    public Long getTemps_restant() {
        return temps_restant;
    }

    public void setTemps_restant(Long temps_restant) {
        this.temps_restant = temps_restant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
