package com.crypto.controller.backoffice.commission;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crypto.model.commission.Commission;
import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.model.crypto.TransactionCrypto;
import com.crypto.model.commission.analyse.Analyseur;
import com.crypto.service.connection.UtilDB;

@RequestMapping("/crypto")
@Controller
public class CommissionController {

    @Autowired
    UtilDB utilDB ;
    
    @PostMapping("/commission/modifier")
    public String modifCommission(
            RedirectAttributes redirectAttributes, String idCryptomonnaie, 
            double pourcentage) {

        try(Connection connection = utilDB.getConnection()) {
            Cryptomonnaie crypto = new Cryptomonnaie();
            crypto.setId(idCryptomonnaie);
            Commission com = new Commission();
            com.setCryptomonnaie(crypto);
            com.setPourcentage(pourcentage);
            com.modifierCommission(connection);
            redirectAttributes.addFlashAttribute("message", "Commission modifiée avec succès !");
        } catch (Exception err) {
            redirectAttributes.addFlashAttribute("message", "Erreur : " + err.getMessage());
        }

        return "redirect:/crypto/commission/modification";
    }

    @PostMapping("/commission/analyser")
    public String traiterAnalyse(String idTypeAnalyse, 
        String dateHeureMin, String dateHeureMax, 
        @RequestParam(name = "idCrypto", required = false) String idCryptomonnaie, 
        RedirectAttributes redirectAttributes) {
        try(Connection connection = this.utilDB.getConnection()) {
            Analyseur analyseur = new Analyseur(idTypeAnalyse, dateHeureMin, dateHeureMax, idCryptomonnaie);
            TransactionCrypto [] transactions = analyseur.faireAnalyse(connection);
            redirectAttributes.addFlashAttribute("transactions", transactions);
        }
        catch(Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            e.printStackTrace(); 
        }
        return "redirect:/crypto/commission/analyse";
    }

}
