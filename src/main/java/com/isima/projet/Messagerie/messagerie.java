/*
package com.isima.projet.Messagerie;

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
public class messagerie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_mess;
    private String message;
    private String etat;
    private Date  date_envoie;


    @ManyToOne
    private Client client;

    @ManyToOne

    private Entreprise entreprise;


}
*/
