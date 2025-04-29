package com.crypto.service.mail ;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    // smtp://accessapi22@gmail.com:idqkooeujlpgtmco@smtp.gmail.com:587
    // private final String smtpHost = "smtp.gmail.com"; // Adresse du serveur SMTP
    // private final String smtpPort = "587";                    // Port SMTP (par ex., 587 pour TLS)
    // private final String senderEmail = "accessapi22@gmail.com"; // Email expéditeur
    // private final String senderPassword = "idqkooeujlpgtmco";          // Mot de passe de l'expéditeur

    // public void envoyerMail(String recepteur, Map<String, Object> contexte, String contenu, String objet) 
    //         throws MessagingException {
    //     // Configuration des propriétés du serveur SMTP
    //     Properties properties = new Properties();
    //     properties.put("mail.smtp.host", smtpHost);
    //     properties.put("mail.smtp.port", smtpPort);
    //     properties.put("mail.smtp.auth", "true");
    //     properties.put("mail.smtp.starttls.enable", "true");

    //     // Authentification de l'expéditeur
    //     Session session = Session.getInstance(properties, new Authenticator() {
    //         @Override
    //         protected PasswordAuthentication getPasswordAuthentication() {
    //             return new PasswordAuthentication(senderEmail, senderPassword);
    //         }
    //     });

    //     try {
    //         // Création du message
    //         MimeMessage message = new MimeMessage(session);
    //         message.setFrom(new InternetAddress(senderEmail));
    //         message.addRecipient(Message.RecipientType.TO, new InternetAddress(recepteur));
    //         message.setSubject(objet);

    //         // Remplir le contenu avec le contexte
    //         String body = remplirTemplate(contenu, contexte);

    //         // Ajout du contenu HTML
    //         message.setContent(body, "text/html");

    //         // Envoi du message
    //         Transport.send(message);
    //         System.out.println("Email envoyé avec succès !");
    //     } catch (MessagingException e) {
    //         throw new RuntimeException("Impossible d'envoyer l'email : " + e.getMessage(), e);
    //     }
    // }

    // /**
    //  * Fonction utilitaire pour remplir le template avec le contexte.
    //  */
    // private String remplirTemplate(String template, Map<String, Object> contexte) {
    //     for (Map.Entry<String, Object> entry : contexte.entrySet()) {
    //         template = template.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
    //     }
    //     return template;
    // }
}
