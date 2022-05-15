package com.isima.projet.Avis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isima.projet.Entreprise.Entreprise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "EntrepriseId")
    @OnDelete(action = OnDeleteAction.CASCADE)

    private Entreprise provider;
    @JsonIgnore
    public void setEntreprise(Entreprise provider) {
        this.provider = provider;}
    @JsonIgnore
    public Entreprise getEntreprise() {
        return provider;
    }

  /*  @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "client_id", nullable = false, unique = true)
    private Client client;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "entreprise_id", nullable = false, unique = true)
    private Entreprise entreprise;

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }*/
 /*@JsonProperty("entreprise")
  public Long getEntrepriseId() {
      return entreprise.getId();
  }
    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }*/
}
