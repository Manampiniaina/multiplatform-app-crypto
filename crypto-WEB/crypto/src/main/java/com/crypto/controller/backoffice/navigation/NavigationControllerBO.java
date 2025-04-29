package com.crypto.controller.backoffice.navigation;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crypto.model.commission.analyse.Analyseur;
import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.model.crypto.analyse.TypeAnalyse;
import com.crypto.model.fond.MouvementFondAttente;
import com.crypto.model.utilisateur.Utilisateur;
import com.crypto.service.connection.UtilDB;
import com.google.api.Http;

import jakarta.servlet.http.HttpSession;


@RequestMapping("/crypto")
@Controller
public class NavigationControllerBO {
        
    @Autowired
    private UtilDB utilDB ;

    @GetMapping("/utilisateur/fondEnAttente")
    public String listeFondsEnAttente(Model model) {
        try (Connection co = this.utilDB.getConnection()) {
            MouvementFondAttente mvt = new MouvementFondAttente();
            model.addAttribute("depots", mvt.getDepotsEnAttentes(co));
            model.addAttribute("retraits", mvt.getRetraitsEnAttentes(co));
        }
        catch(Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return new String("pages/backoffice/utilisateur/validationFond");
    }
    

    @GetMapping("/utilisateur/filtre")
    public String getFormFiltreUtilisateur() {
        return "pages/backoffice/utilisateur/filtre";
    }

    @GetMapping("/commission/analyse")
    public String getFormAnalyseCommission(Model model) {
        try (Connection co = this.utilDB.getConnection()) {
            TypeAnalyse [] typesAnalyses = Analyseur.typesAnalyses;
            Cryptomonnaie[] cryptos = Cryptomonnaie.getAll(co);
            model.addAttribute("typesAnalyse", typesAnalyses);
            model.addAttribute("cryptos", cryptos);
        }
        catch(Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "pages/backoffice/commission/analyse";
    }

    @GetMapping("/commission/modification")
    public String formModifCommission(Model model) {
        try(Connection conn= utilDB.getConnection()) { 
            Cryptomonnaie[] ltcrypto=Cryptomonnaie.getAll(conn);
            model.addAttribute("listCrypto",ltcrypto);
        } catch (Exception err) {
            model.addAttribute("message", "Erreur lors de la récupération des données : " + err.getMessage());
        }
        return "pages/backoffice/commission/modification";
    }

    @GetMapping("/admin/deconnection")
    public String deconnection(HttpSession session) {
        session.removeAttribute("admin");
        return "redirect:/";
    }

    @GetMapping("/admin/accueil")
    public String accueil() {
        return "pages/backoffice/accueil"; 
    }

}
