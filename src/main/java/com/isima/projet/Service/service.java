package com.isima.projet.Service;

import com.isima.projet.Rendez_vous.RDV;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id_service;
    private  String lib_service;
    private String description ;
    private  String type;
    private Integer cout;
    private Float  tax;
    @OneToOne
    private RDV rdv;
}
