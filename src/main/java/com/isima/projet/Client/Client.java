package com.isima.projet.Client;

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
import javax.validation.constraints.NotNull;
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
	@NotNull
	private String nom;
	@NotNull
	private String prenom;
	@NotNull
	private String tel;
	@NotNull
	private String adress;
	@NotNull
	private String genre;
	@NotNull
	private String pays;
	@NotNull
	private String ville;
	@NotNull
	private  String email;
	@NotNull private String mdp;
	@NotNull private String images;
	/*@OneToMany(mappedBy = "client",fetch = FetchType.LAZY)

	private List<messagerie> messageries;*/
	@JsonIgnore
	@OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
	private List<RDV>rdv;

	/*@OneToMany(mappedBy = "client",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private List<Avis> avis;*/
/*	@OneToOne(cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Facture facture;*/
	@OneToOne
	@JsonIgnore
	private User users;
	@OneToMany(mappedBy = "client",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private List <Event> event;

	public User getUsers() {
		return users;
	}

	public void setUsers(User users) {
		this.users = users;
	}
}
