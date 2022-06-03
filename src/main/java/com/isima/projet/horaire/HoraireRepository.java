package com.isima.projet.horaire;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoraireRepository extends JpaRepository<horaire,Long> {
    List<horaire> findByEntrepriseId(long id);
}
