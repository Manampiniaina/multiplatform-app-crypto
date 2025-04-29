package com.crypto.controller.frontoffice.crypto;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crypto.model.crypto.ChangementCoursCrypto;
import com.crypto.model.crypto.analyse.Analyseur;
import com.crypto.service.connection.UtilDB;

@RequestMapping("/crypto")
@Controller
public class AnalyseController {
    
    @Autowired
    private UtilDB utilDB ;

    @PostMapping("/analyser")
    public String traiterAnalyse(@RequestParam("idTypeAnalyse") String idTypeAnalyse, 
        @RequestParam("dateHeureMin") String dateHeureMin, @RequestParam("dateHeureMax") String dateHeureMax, 
        @RequestParam(name = "cryptomonnaies[]", required = false) String[] selectedIdCryptos, 
        RedirectAttributes redirectAttributes) {
        try(Connection connection = this.utilDB.getConnection()) {
            Analyseur analyseur = new Analyseur(idTypeAnalyse, dateHeureMin, dateHeureMax, selectedIdCryptos);
            ChangementCoursCrypto [] chgts = analyseur.faireAnalyse(connection);
            redirectAttributes.addFlashAttribute("chgts", chgts);
        }
        catch(Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            e.printStackTrace(); 
        }
        return "redirect:/crypto/form/analyse";
    }

}
