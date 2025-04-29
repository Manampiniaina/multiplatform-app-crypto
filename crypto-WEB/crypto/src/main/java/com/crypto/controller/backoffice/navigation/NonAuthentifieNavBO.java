package com.crypto.controller.backoffice.navigation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.crypto.model.utilisateur.admin.Admin;
import com.crypto.service.connection.UtilDB;

@Controller
public class NonAuthentifieNavBO {

    @Autowired 
    UtilDB utilDB ;


    @GetMapping("/admin/connection")
    public String connection(Model model) {
        Admin admin = new Admin("Rakoto", "rakoto") ;
        model.addAttribute("admin", admin); 
        return "pages/backoffice/connection"; 
    }

}
