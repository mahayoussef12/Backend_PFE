package com.isima.projet.Entreprise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isima.projet.Rendez_vous.RDV;
import com.isima.projet.User.User;
import com.isima.projet.calendrier.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
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
	private String nom_gerant;
	private String tel_gerant;
	private String nomSociete;
	private String Batinda;
	private String tel;
	private  String email;
	private String mdp;
	private String image;
	private String categorie;
	private String ville ;
	private String adresse ;
	private Date CreationEntreprise;
	private String test;
	private LocalDateTime time;

	@JsonIgnore
	@OneToMany(mappedBy = "entreprise",cascade = CascadeType.ALL)
	private List<Event> events;
/*	@OneToMany(mappedBy = "entreprise")
	private List<messagerie> messagerieList;*/
@JsonIgnore
	@OneToMany(mappedBy = "entreprise",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
@OnDelete(action = OnDeleteAction.CASCADE)
	private  List<RDV>rdv;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User users;




}




