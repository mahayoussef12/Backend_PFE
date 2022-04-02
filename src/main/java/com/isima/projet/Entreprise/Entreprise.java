package com.isima.projet.Entreprise;

import com.isima.projet.Avis.Avis;
import com.isima.projet.Messagerie.messagerie;
import com.isima.projet.Rendez_vous.RDV;
import com.isima.projet.User.utilisateur;
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
public class Entreprise {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String mat_fiscale;
	private String Batinda;
	private String nom_gerant;
	private String tel_gerant;
	private String nom;
	private String prenom;
	private String tel;
	private  String email;
	private String mdp;
	private String image;
	private String categorie;
	private String ville ;
	//private String Jours;
	//private  String heure;
	@OneToMany(mappedBy = "entreprise")
	private List<messagerie> messagerieList;
	@OneToMany(mappedBy = "entreprise")
	private  List<RDV>rdv;
	@OneToMany(mappedBy = "entreprise")
	private  List<Avis>avis;
	@OneToOne
	private utilisateur users;




}




