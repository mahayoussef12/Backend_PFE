package com.isima.projet.Avis;

import com.isima.projet.Client.Client;
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
    @Column(name="auteur")
    private String nom_auteur;
    @Column(name="description")
    private String description;


    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "client_id", nullable = false, unique = true)
    private Client client;

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
    }
}