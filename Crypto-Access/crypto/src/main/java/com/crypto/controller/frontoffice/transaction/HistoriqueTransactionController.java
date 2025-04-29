package com.crypto.controller.frontoffice.transaction;

import java.sql.Connection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.model.transaction.Transaction;
import com.crypto.model.utilisateur.Utilisateur;
import com.crypto.service.connection.UtilDB;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/crypto")
@Controller
public class HistoriqueTransactionController { 
    
    @Autowired
    UtilDB utilDB;

    @GetMapping("/detailUtilisateur/transactions")
    public String getDetailsUtilisateur(String idUtilisateur, Model model) {
        try (Connection co = this.utilDB.getConnection()) {
            Utilisateur utilisateur = Utilisateur.getById(co, idUtilisateur);
            Transaction transaction = new Transaction();
            transaction.setHistoriqueTransactionsByCriteria(co, "", "",utilisateur.getId(), "");    
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("transaction", transaction);
        }
        catch(Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "pages/frontoffice/accueil/historique/detailHistoriqueUtilisateur";
    }
    

    @PostMapping("/historique/transactions")
    public String rechercherOperationsParCritere(String dateHeureMin, String dateHeureMax, String idUtilisateur, String idCrypto, Model model) {
        try(Connection co = this.utilDB.getConnection()) {
            Transaction transaction = new Transaction();
            transaction.setHistoriqueTransactionsByCriteria(co, dateHeureMin, dateHeureMax, idUtilisateur, idCrypto);
            Cryptomonnaie [] cryptos = Cryptomonnaie.getAll(co);
            List<Utilisateur> utilisateurs = Utilisateur.getAll(co);
            model.addAttribute("utilisateurs", utilisateurs);
            model.addAttribute("cryptos", cryptos);
            model.addAttribute("transaction", transaction);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "pages/frontoffice/accueil/historique/historiqueTransaction";
    }
    


}
