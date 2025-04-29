package com.crypto.controller.frontoffice.transaction;

import com.crypto.config.DonneesConfig;
import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.model.firebase.AchatVente;
import com.crypto.model.fond.Fond;
import com.crypto.model.portefeuille.PorteFeuilleDetails;
import com.crypto.model.utilisateur.Utilisateur;
import com.crypto.service.connection.UtilDB;
import com.crypto.service.firebase.FirestoreTransaction;
import com.crypto.service.firebase.FirestoreUtilisateur;
import com.crypto.service.util.Util;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/crypto")
public class TransactionController {

    @Autowired
    UtilDB utilDB ;

    @Autowired 
    FirestoreTransaction firestoreTransaction ; 

    @Autowired 
    FirestoreUtilisateur firestoreUtilisateur ;

    @PostMapping("/vente/valider")
    public String validerVente(RedirectAttributes redirectAttributes,
            @RequestParam("quantity") int quantity,
            @RequestParam("idportefeuilledetail") String idportefeuilledetail,
            @RequestParam("date")LocalDateTime dateTransaction, HttpSession session) {

        try(Connection connection = this.utilDB.getConnection()) {
            PorteFeuilleDetails portefeuilleDetail = PorteFeuilleDetails.getById(idportefeuilledetail, connection);
            Utilisateur u = (Utilisateur) session.getAttribute("utilisateur");
            // Utilisateur u=new Utilisateur();
            // u.setId(DonneesConfig.tempIdUtilisateur);
            u.setFond(Fond.getFondByUtilisateur(u, connection));
            double mttObtenu = u.vendre(connection,portefeuilleDetail,quantity,dateTransaction);

            AchatVente achatVente = new AchatVente(u, new Utilisateur(),portefeuilleDetail.getCryptomonnaie(), dateTransaction, quantity*-1);
            envoyerVersFirebase(achatVente) ;

            
            firestoreUtilisateur.envoyerFond(Util.getMap(u, mttObtenu), "fonds");
            
            redirectAttributes.addFlashAttribute("message", "Vente effectuée avec succès !");
        } 
        catch (Exception err) {
            redirectAttributes.addFlashAttribute("message", "Erreur : " + err.getMessage());
        }
        return "redirect:/crypto/vente";
    }
    

    @PostMapping("/achat/acheter")
    public String acheter(
                RedirectAttributes redirectAttributes,
                @RequestParam("quantite") int quantity,
                @RequestParam("idCrypto") String idcryptommonaie,
                @RequestParam("dateAchat") LocalDateTime dateTransaction, HttpSession session) {

            try(Connection connection = utilDB.getConnection()) {
                Cryptomonnaie crypto=Cryptomonnaie.getById(connection,idcryptommonaie);
                Utilisateur u = (Utilisateur) session.getAttribute("utilisateur");
                // Utilisateur u = new Utilisateur(DonneesConfig.tempIdUtilisateur);
                u.setFond(Fond.getFondByUtilisateur(u, connection));
                u.setPorteFeuilleByConnection(connection);
                double mttSoustrait = u.traiterAchat(connection,crypto,quantity,dateTransaction);

                AchatVente achatVente = new AchatVente(new Utilisateur(), u,  crypto, dateTransaction, quantity);
                envoyerVersFirebase(achatVente) ;

                firestoreUtilisateur.envoyerFond(Util.getMap(u, mttSoustrait), "fonds");

                redirectAttributes.addFlashAttribute("message", "Achat effectuée avec succès !");
            } catch (Exception err) {
                err.printStackTrace();
                redirectAttributes.addFlashAttribute("message", "Erreur : " + err.getMessage());
            }

            return "redirect:/crypto/detailCrypto?idCrypto="+idcryptommonaie;
        }
    
    void envoyerVersFirebase(AchatVente achatVente) throws Exception{
        firestoreTransaction.setAchatVente(achatVente);
        firestoreTransaction.synchroniser();
    }

}
