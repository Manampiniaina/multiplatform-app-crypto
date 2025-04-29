// package com.crypto.service.schedule;

// import java.sql.Connection;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.config.ScheduledTaskRegistrar;
// import org.springframework.scheduling.annotation.SchedulingConfigurer;
// import org.springframework.stereotype.Component;

// import com.crypto.config.DonneesConfig;
// import com.crypto.service.connection.UtilDB;
// import com.crypto.service.firebase.FirestoreRecuperation;

// @Component
// public class SynchronisationSchedule implements SchedulingConfigurer {

//     @Autowired
//     private UtilDB utilDB;

//     @Autowired
//     private FirestoreRecuperation firestoreRecuperation;

//     @Autowired
//     private DonneesConfig donneesConfig;

//     private long frequence;

//     @Autowired
//     public SynchronisationSchedule(UtilDB utilDB, FirestoreRecuperation firestoreRecuperation, DonneesConfig donneesConfig) {
//         this.utilDB = utilDB;
//         this.firestoreRecuperation = firestoreRecuperation;
//         this.donneesConfig = donneesConfig;
//         initializeFrequence();
//     }

//     private void initializeFrequence() {
//         try (Connection connection = utilDB.getConnection()) {
//             frequence = donneesConfig.getFrequenceFirebase(connection);
//             System.out.println("Frequence est " + frequence);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     @Override
//     public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//         taskRegistrar.addFixedRateTask(() -> {
//             try (Connection connection = utilDB.getConnection()) {
//                 firestoreRecuperation.synchroniser(connection);
//             } catch (Exception e) {
//                 e.printStackTrace();
//             }
//         }, frequence);
//     }
// }
