package com.crypto.service.util;

import java.util.HashMap;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class DateGestion {
    
        // Dictionnaire pour les mois en français
    private static final Map<String, Integer> MONTHS = new HashMap<>();
    
    static {
        MONTHS.put("janvier", 1);
        MONTHS.put("février", 2);
        MONTHS.put("mars", 3);
        MONTHS.put("avril", 4);
        MONTHS.put("mai", 5);
        MONTHS.put("juin", 6);
        MONTHS.put("juillet", 7);
        MONTHS.put("août", 8);
        MONTHS.put("septembre", 9);
        MONTHS.put("octobre", 10);
        MONTHS.put("novembre", 11);
        MONTHS.put("décembre", 12);
    }

    public static Timestamp gerer(String dateStr) {
        try {
            // Séparer la date et l'heure
            String[] parts = dateStr.split("\\s*à\\s*");
            String[] dateParts = parts[0].split(" ");
            String[] timeParts = parts[1].split(" UTC");

            // Extraire les composants de la date
            int day = Integer.parseInt(dateParts[0]);
            int month = MONTHS.get(dateParts[1]); // Convertir le mois en nombre
            int year = Integer.parseInt(dateParts[2]);

            // Extraire l'heure
            String[] time = timeParts[0].split(":");
            int hour = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);
            int second = Integer.parseInt(time[2]);

            // Extraire le décalage UTC
            int utcOffset = Integer.parseInt(timeParts[1]); // UTC+3 → 3

            // Convertir en millisecondes depuis Epoch
            long timestampMillis = new java.util.GregorianCalendar(year, month - 1, day, hour, minute, second)
                    .getTimeInMillis();

            return retirerFuseauHoraire(new Timestamp(timestampMillis), utcOffset);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du parsing de la date : " + dateStr, e);
        }
    }

    public static Timestamp retirerFuseauHoraire(Timestamp timestamp, int diff) {
        if (timestamp == null) {
            throw new IllegalArgumentException("Le timestamp ne peut pas être null");
        }

        // Convertir le Timestamp en LocalDateTime en utilisant le même Instant sans appliquer de changement d'heure
        LocalDateTime localDateTime = timestamp.toInstant()
                                               .atZone(ZoneId.of("UTC")) // Zone UTC pour ignorer le fuseau
                                               .toLocalDateTime();

        // Ajouter la différence à l'heure
        localDateTime = localDateTime.plusHours(0);  // Utilise plusMinutes(diff) si tu veux un décalage en minutes

        // Retourner un Timestamp sans fuseau horaire, mais avec la nouvelle heure
        return Timestamp.valueOf(localDateTime);
    }

}
