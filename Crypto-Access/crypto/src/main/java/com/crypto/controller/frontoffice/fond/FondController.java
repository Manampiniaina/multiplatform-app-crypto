package com.crypto.controller.frontoffice.fond;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crypto.config.DonneesConfig;
import com.crypto.model.fond.MouvementFond;
import com.crypto.model.utilisateur.Utilisateur;
import com.crypto.service.connection.UtilDB;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/crypto")
@Controller
public class FondController {

    @Autowired
    UtilDB utilDB ;

    @PostMapping("/demande/fond")
    public String demanderFond(RedirectAttributes redirectAttributes, HttpSession session, String typeDemande, 
    String montant, String dateDemande) {
        try(Connection connection = utilDB.getConnection()) {
            MouvementFond mvt = new MouvementFond(typeDemande, montant, dateDemande);
            Utilisateur userActu = (Utilisateur)session.getAttribute("utilisateur");
            // Utilisateur userActu = new Utilisateur(DonneesConfig.tempIdUtilisateur);
            userActu.demandeActionFond(connection, mvt);
            redirectAttributes.addFlashAttribute("message", "Succès : Demande actuellement en cours de validation");
        } catch (Exception err) {
            redirectAttributes.addFlashAttribute("message", "Erreur : " + err.getMessage());
        }

        return "redirect:/crypto/demande/fond";
    }

}
