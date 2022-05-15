package com.isima.projet.Super_Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class Super_adminController {

    @Autowired
    Super_adminRepository repo;

    @PostMapping("/adminadmin")
    private  Super_admin  admin(Super_admin ad) {
        ad.setEmail("admin@gmail.com");
        ad.setName("admin");
        repo.save(ad);
        return ad;
    }
    @GetMapping("/admin")
    private List<Super_admin> getall() {
        return repo.findAll();
    }

    private Super_admin updateAdmin(@PathVariable Integer id, @RequestBody Super_admin ad) {
        if (id != null) {

            return repo.save(ad);
        }
        return ad;
    }
}
