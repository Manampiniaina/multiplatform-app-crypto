package com.crypto.controller.backoffice.authentification;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crypto.model.utilisateur.admin.Admin;
import com.crypto.service.connection.UtilDB;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/admin")
@Controller
public class AuthentificationControllerBO {
 
    @Autowired 
    UtilDB utilDB ;

    @PostMapping("/connection")
    public String connection(@ModelAttribute Admin admin, RedirectAttributes redirectAttributes, Model model, HttpSession session) {
        String cheminRedirection = "redirect:/admin/connection";
        try(Connection connection = utilDB.getConnection()) {
            admin.se_connecter(connection);
            session.setAttribute("admin", admin);
            redirectAttributes.addFlashAttribute("message","Connection réussie");
            cheminRedirection = "redirect:/crypto/admin/accueil";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return cheminRedirection ;

    }
    
}