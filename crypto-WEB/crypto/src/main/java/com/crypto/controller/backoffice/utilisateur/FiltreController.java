package com.crypto.controller.backoffice.utilisateur;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crypto.model.utilisateur.filtre.Resultat;
import com.crypto.service.connection.UtilDB;

@RequestMapping("/crypto")
@Controller
public class FiltreController {

    @Autowired
    UtilDB utilDB ;

    @PostMapping("/utilisateur/filtrer")
    public String filtrerUtilisateur(@RequestParam(name = "dateHeureMax", required = false) LocalDateTime dateMax, RedirectAttributes redirectAttributes) throws Exception {
        try (Connection connection = utilDB.getConnection()) {
            List<Resultat> resultats = Resultat.getResultatByDate(dateMax,connection);
            redirectAttributes.addFlashAttribute("resultats", resultats);
        }
        catch(Exception err) {
            redirectAttributes.addFlashAttribute("message", err.getMessage());
        }
        return "redirect:/crypto/utilisateur/filtre";
    }
    
}
