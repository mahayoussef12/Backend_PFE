package com.isima.projet.Avis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isima.projet.Entreprise.Entreprise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Schema(name = "Avis", description = "user entity definition")
public class Avis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_avis;
    private int start;
    private String nom_auteur;
    private String prenom_auteur;
    private String email_auteur;
    private String description;
    @JsonIgnore
    private Boolean afficher;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "EntrepriseId")


    private Entreprise provider;
    @JsonIgnore
    public void setEntreprise(Entreprise provider) {
        this.provider = provider;}
    @JsonIgnore
    public Entreprise getEntreprise() {
        return provider;
    }

}
