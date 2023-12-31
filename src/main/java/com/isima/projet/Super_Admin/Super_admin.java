package com.isima.projet.Super_Admin;

import com.isima.projet.Rendez_vous.RDV;
import com.isima.projet.User.User;
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
public class Super_admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_admin;
    private  String email;
    private String name;
    @OneToMany(mappedBy = "super_admin",fetch = FetchType.LAZY)
    private List<RDV> rdv;
  /*  @OneToMany(mappedBy = "super_admin",fetch = FetchType.LAZY)
    public List<Facture> facture;*/
    @OneToMany(mappedBy = "super_admin",fetch = FetchType.LAZY)
    private List<User> Users;
}
