package com.isima.projet.calendrier.domain;

import com.isima.projet.Client.Client;
import com.isima.projet.Entreprise.Entreprise;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Event {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	String text;
	
	LocalDateTime start;
	
	LocalDateTime end;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
	@ManyToOne(cascade = CascadeType.REMOVE)
	private Client client;

	@ManyToOne(cascade = CascadeType.REMOVE)
	private Entreprise entreprise;
	
	
}
