package com.isima.projet.Client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isima.projet.Avis.Avis;
import com.isima.projet.Facture.Facture;

import com.isima.projet.Rendez_vous.RDV;
import com.isima.projet.User.User;
import com.isima.projet.calendrier.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nom;
	private String prenom;
	private String tel;
	private String adress;
	private String genre;
	private String pays;
	private String ville;
	private  String email;
	private String mdp;
	private String images;
	/*@OneToMany(mappedBy = "client",fetch = FetchType.LAZY)

	private List<messagerie> messageries;*/
	@JsonIgnore
	@OneToMany(mappedBy = "client")
	private List<RDV>rdv;

	@OneToMany(mappedBy = "client")
	private List<Avis> avis;

	@OneToOne
	private Facture facture;
	@OneToOne
	private User users;
	@OneToMany(mappedBy = "client")
	private List <Event> event;


}
