package com.crypto.controller.backoffice.utilisateur;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crypto.model.fond.MouvementFondAttente;
import com.crypto.model.utilisateur.admin.Admin;
import com.crypto.service.connection.UtilDB;
import com.crypto.service.firebase.FirestoreUtilisateur;
import com.crypto.service.util.Util;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;


@RequestMapping("/crypto")
@Controller
public class ValidationFondController {
            
    @Autowired
    private UtilDB utilDB ;

    @Autowired 
    FirestoreUtilisateur firestoreUtilisateur;
    
    @GetMapping("/utilisateur/validerFond")
    public String validerFondUtilisateur(@RequestParam("idFondAttente") String idFond, RedirectAttributes redirectAttributes, HttpSession session) {
        try (Connection co = this.utilDB.getConnection()) {
            Admin admin =(Admin) session.getAttribute("admin");
            MouvementFondAttente mvt = new MouvementFondAttente(idFond);
            MouvementFondAttente theMvt = mvt.getById(co);
            admin.validerDemandeFond(co, theMvt);

            firestoreUtilisateur.setUtilisateur(theMvt.getUtilisateur());
            firestoreUtilisateur.envoyerFond(Util.getMap(theMvt.getUtilisateur(), theMvt.getMontant()), "fonds");


            redirectAttributes.addFlashAttribute("message", "Succès : Demande de fond validé");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/crypto/utilisateur/fondEnAttente";
    }
    

}
