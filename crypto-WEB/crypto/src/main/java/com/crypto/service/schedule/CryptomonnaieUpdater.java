package com.crypto.service.schedule;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.crypto.config.DonneesConfig;
import com.crypto.model.crypto.ChangementCoursCrypto;
import com.crypto.model.crypto.Cryptomonnaie;
import com.crypto.service.connection.UtilDB;
import com.crypto.service.firebase.FirestoreCryptomonnaie;
import com.crypto.service.schedule.socket.CoursHandler;
import com.crypto.service.util.Wrapper;

@Component
public class CryptomonnaieUpdater {
    
    @Autowired
    UtilDB utilDB ;

    @Autowired
    private CoursHandler coursHandler;

    @Autowired
    FirestoreCryptomonnaie firestoreCryptomonnaie ;

    @Scheduled(fixedRate = DonneesConfig.FREQUENCE_CHANGEMENT) // Toutes les 10 secondes
    public void changerCours() throws Exception{

        Cryptomonnaie cryptomonnaie = new Cryptomonnaie();
        try(Connection connection = utilDB.getConnection()){
            ChangementCoursCrypto[] changementCoursCryptos = cryptomonnaie.nouveauCours(connection);

            firestoreCryptomonnaie.setCryptomonnaies(changementCoursCryptos);
            firestoreCryptomonnaie.synchroniser();
            
            coursHandler.signal(donnerChangementCours(connection));
        } catch(Exception err) {
            throw err ;
        }
        // System.out.println("Cours des cryptomonnaies mis à jour.");
    }

    String donnerChangementCours(Connection connection) throws Exception{
        
        ChangementCoursCrypto[] changementCoursCryptos = ChangementCoursCrypto.getByCriteria(connection, DonneesConfig.SECONDES_CONSIDEREE);
        Map<String, List<ChangementCoursCrypto>> map = new HashMap<>();

        for (ChangementCoursCrypto changement : changementCoursCryptos) {
            String cryptoNom = changement.getCryptomonnaie().getNom(); 
            map.computeIfAbsent(cryptoNom, k -> new ArrayList<>());
            map.get(cryptoNom).add(changement);
        }
        Map.Entry<String, List<ChangementCoursCrypto>> entrees = map.entrySet().iterator().next();
        List<Long> temps = new ArrayList<>();
        for (ChangementCoursCrypto changementCoursCrypto : map.get(entrees.getKey())) {
            temps.add(changementCoursCrypto.getDate().getTime());
        }
        ChangementData changementData = new ChangementData(temps, map);
        return new Wrapper().enJSON(changementData) ;
    }
}


class ChangementData {

    private List<Long> temps;
    private Map<String, List<ChangementCoursCrypto>> changes;

    // Constructeur
    public ChangementData(List<Long> temps, Map<String, List<ChangementCoursCrypto>> changes) {
        setTemps(temps);
        setChanges(changes);
    }

    // Getters et Setters
    public List<Long> getTemps() {
        return temps;
    }

    public void setTemps(List<Long> temps) {
        this.temps = temps;
    }

    public Map<String, List<ChangementCoursCrypto>> getChanges() {
        return changes;
    }

    public void setChanges(Map<String, List<ChangementCoursCrypto>> changes) {
        this.changes = changes;
    }
}