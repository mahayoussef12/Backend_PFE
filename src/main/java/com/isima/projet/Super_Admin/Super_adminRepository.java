package com.isima.projet.Super_Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Super_adminRepository extends JpaRepository<Super_admin,Integer> {
    @Query(value = "select * from super_admin", nativeQuery = true)
    Super_admin test ();

}
