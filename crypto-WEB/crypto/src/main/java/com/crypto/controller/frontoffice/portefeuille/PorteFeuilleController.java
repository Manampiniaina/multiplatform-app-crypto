package com.crypto.controller.frontoffice.portefeuille;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import  com.crypto.model.vente.*;
import  com.crypto.model.portefeuille.*;
import com.crypto.service.connection.UtilDB;
import com.crypto.service.util.Util;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/crypto")
@Controller
public class PorteFeuilleController {
    @Autowired
    private UtilDB utilDB ;

    // @GetMapping("/portefeuille/vendre")
    // public String vendre(
    //         RedirectAttributes redirectAttributes,
    //         @RequestParam("quantity") int quantity,
    //         @RequestParam("idportefeuilledetail") String idportefeuilledetail) {

    //     try {
    //         Connection connection = utilDB.getConnection();
    //         PorteFeuilleDetails portefeuilleDetail = PorteFeuilleDetails.getById(idportefeuilledetail, connection);

    //         if (portefeuilleDetail.getQuantite() >= quantity) {
    //             Vente vente = new Vente();
    //             vente.setQuantiteVendu(quantity);
    //             // vente.setDateVente(Date.valueOf("2025-01-10"));
    //             vente.setDateVente(Util.getDateActuelle());
    //             vente.setD_prixVente(portefeuilleDetail.getCryptomonnaie().getValeur());
    //             vente.setPortefeuilleDetail(portefeuilleDetail);

    //             Vente.insert(vente, connection);
    //             PorteFeuilleDetails.updateQuantite(
    //                     portefeuilleDetail.getId(),
    //                     portefeuilleDetail.getQuantite() - quantity,
    //                     connection
    //             );

    //             redirectAttributes.addFlashAttribute("message", "Vente effectuée avec succès !");
    //         } else {
    //             redirectAttributes.addFlashAttribute("message", "Quantité insuffisante.");
    //         }
    //     } catch (Exception err) {
    //         redirectAttributes.addFlashAttribute("message", "Erreur : " + err.getMessage());
    //     }

    //     return "redirect:/portefeuille";
    // }

}
