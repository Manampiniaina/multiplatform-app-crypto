package com.crypto.controller.frontoffice.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.service.mail.EmailService;

import jakarta.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EmailController {

    // @Autowired
    // private EmailService emailService;

    // @Value("${app.base-url}")
    // private String baseUrl;


    // /**
    //  * Traite l'envoi de l'email.
    //  */
    // @GetMapping("/envoyer-mail")
    // public String envoyerMail(Model model, String idAchat) {
    //     try {
    //         String url = baseUrl+"/confirmerAchat?idAchat="+idAchat;
    //         String recepteur = "vetsojoella@gmail.com";
    //         String objet = "Mail de confirmation d'achat";
    //         String contenu = "<p>Bonjour,</p>"
    //                 + "<p>Veuillez cliquer sur le lien suivant pour confirmer votre inscription :</p>"
    //                 + "<a href='{{url}}'>Confirmer mon inscription</a>"
    //                 + "<p>Cordialement,</p>";

    //         // Contexte avec le lien
    //         Map<String, Object> contexte = new HashMap<>();
    //         contexte.put("url", url);
    //         // Envoi de l'email
    //         emailService.envoyerMail(recepteur, contexte, contenu, objet);

    //         // Ajout d'un message de succès pour la vue
    //         return("message : Email envoyé avec succès !");

    //     } catch (MessagingException e) {
    //         // Gestion des erreurs avec un message pour la vue
    //         return("erreur Échec de l'envoi de l'email : " + e.getMessage());
    //     }
        
    // }
}

