package com.isima.projet.horaire;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isima.projet.Entreprise.Entreprise;
import lombok.*;

import javax.persistence.*;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class horaire {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String opentime;
    private  String closeTime;
    private String opentimemidi;
    private String closetimemidi;
    private String Day;
    @JsonIgnore
     @ManyToOne
    private Entreprise entreprise;
@JsonIgnore
    public Entreprise getEntreprise() {
        return entreprise;
    }
@JsonIgnore
    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }
}
