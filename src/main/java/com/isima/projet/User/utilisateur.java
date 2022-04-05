package com.isima.projet.User;

import com.isima.projet.Client.Client;
import com.isima.projet.Entreprise.Entreprise;
import com.isima.projet.Super_Admin.Super_admin;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
public  class utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private  String email;
    private String mdp;
    @ManyToOne
    private Super_admin super_admin;

    @OneToOne
    private Client client;
    @OneToOne
    private Entreprise entreprise;
}


