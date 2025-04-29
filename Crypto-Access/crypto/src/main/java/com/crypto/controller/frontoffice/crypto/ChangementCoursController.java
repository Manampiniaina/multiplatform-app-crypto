package com.crypto.controller.frontoffice.crypto;

import org.springframework.web.bind.annotation.RestController;

import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.service.connection.UtilDB;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/crypto")
public class ChangementCoursController {
    
    @Autowired
    UtilDB utilDB ;

    @GetMapping("/changerCours")
    public String changerCours(@RequestParam String param) {

        Cryptomonnaie cryptomonnaie = new Cryptomonnaie();
        try(Connection conn = this.utilDB.getConnection()){
            cryptomonnaie.nouveauCours(conn);
        } catch(Exception err) {
            return new String(err.getMessage());
        }
        return null ;
    }
    
}
