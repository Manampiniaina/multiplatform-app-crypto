package com.crypto.service.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import com.crypto.model.utilisateur.Utilisateur;

public class Util {

    public static Map<String, Object> getMap(Utilisateur utilisateur, double montant) {
        Map<String, Object> map = new HashMap<>();
        map.put("utilisateur", utilisateur.getId());
        map.put("montant", montant);
        return map;
    }
    public static Timestamp getDateTimeActuelle() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return Timestamp.valueOf(localDateTime);
    }

    public static Timestamp formatDateTimeLocalToTimestamp(String date) throws Exception {
        Timestamp datyLera = null;
        try {
            String formattedDate = date.replace("T", " ") + ":00";
            datyLera = Timestamp.valueOf(formattedDate);
        } catch (Exception e) {
            throw new Exception("Format invalide pour la date-heure : "+date);
        }
        return datyLera;
    }
    
    public static Timestamp getDateHeureMaintenant(){
        LocalDateTime now = LocalDateTime.now();
        Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
        Timestamp timestamp = Timestamp.from(instant);
        return timestamp;
    }

    public static Date getDateActuelle() {
        // Obtient la date actuelle au format LocalDate
        LocalDate localDate = LocalDate.now();

        // Convertit LocalDate en java.sql.Date
        return Date.valueOf(localDate);
    }
}
