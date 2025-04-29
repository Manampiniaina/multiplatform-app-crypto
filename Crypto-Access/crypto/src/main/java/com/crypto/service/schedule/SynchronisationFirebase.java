package com.crypto.service.schedule;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.crypto.config.DonneesConfig;
import com.crypto.service.connection.UtilDB;
import com.crypto.service.firebase.FirestoreRecuperation;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class SynchronisationFirebase {

    @Autowired
    UtilDB utilDB;

    @Autowired
    FirestoreRecuperation firestoreRecuperation;

    @Scheduled(fixedRate = DonneesConfig.FREQUENCE_FIREBASE) // Toutes les 10 secondes
    public void synchronisation() throws Exception {
        System.out.println("Synchronisation ...");
        try (Connection connection = utilDB.getConnection()) {
            firestoreRecuperation.synchroniser(connection);
        } catch (Exception e) {
            throw e;
        }
    }
}
