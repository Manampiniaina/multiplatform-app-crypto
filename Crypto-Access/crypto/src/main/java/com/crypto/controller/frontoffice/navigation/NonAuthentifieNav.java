package com.crypto.controller.frontoffice.navigation;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.crypto.model.utilisateur.Genre;
import com.crypto.model.utilisateur.Utilisateur;
import com.crypto.service.AccessAPI;
import com.crypto.service.connection.UtilDB;

@Controller
public class NonAuthentifieNav {

    @Autowired
    private AccessAPI accessAPI ;

    @Autowired
    private UtilDB utilDB ;

    @GetMapping("/")
    public String choixConnect() {

        return "pages/choixConnect"; 
    }

    @GetMapping("/connection")
    public String connection(Model model ) {

        Utilisateur utilisateur = new Utilisateur() ;
        utilisateur.setGenre(new Genre());
        utilisateur.setMail("leadupuis@gmail.com");
        utilisateur.setMdp("Lea15031992");
        model.addAttribute("utilisateur", utilisateur); 
        return "pages/frontoffice/utilisateur/connection"; // Utilise home.html avec le layout
    }

    @GetMapping("/confirmationPIN")
    public String confirmationPIN() {

        return "pages/frontoffice/utilisateur/confirmationPIN"; 
    }


    @GetMapping("/inscription")
    public String inscription(Model model) {
         try(Connection connection = utilDB.getConnection()){
            model.addAttribute("genres", accessAPI.listeGenres());
        } catch(Exception err) {
            model.addAttribute("message", err.getMessage());
        }
        Utilisateur utilisateur = new Utilisateur() ;
        utilisateur.setGenre(new Genre());
        model.addAttribute("utilisateur", utilisateur); 
        return "pages/frontoffice/utilisateur/inscription"; // Utilise home.html avec le layout
    }

}
