CREATE TABLE Utilisateur(
   id VARCHAR(14)  DEFAULT ('USR') || LPAD(NEXTVAL('s_Utilisateur')::TEXT, 9, '0'),
   mail VARCHAR(50)  NOT NULL,
   nom VARCHAR(100)  NOT NULL,
   prenom VARCHAR(30) ,
   date_naissance DATE NOT NULL,
   lienImage VARCHAR(200) ,
   PRIMARY KEY(id),
   UNIQUE(mail)
);

CREATE TABLE cryptomonnaie(
   id VARCHAR(50)  DEFAULT ('CRYPTO') || LPAD(NEXTVAL('s_crypto')::TEXT, 9, '0'),
   d_valeur NUMERIC(15,2)   default 0,
   nom VARCHAR(50)  NOT NULL,
   d_commission NUMERIC(15,2)   default 0,
   PRIMARY KEY(id)
);

CREATE TABLE portefeuille(
   id VARCHAR(50)  DEFAULT ('PTF') || LPAD(NEXTVAL('s_portefeuille')::TEXT, 9, '0'),
   idUtilisateur VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(idUtilisateur),
   FOREIGN KEY(idUtilisateur) REFERENCES Utilisateur(id)
);

CREATE TABLE portefeuille_detail(
   id VARCHAR(50)  DEFAULT ('PTF_DTL') || LPAD(NEXTVAL('s_portefeuille_detail')::TEXT, 9, '0'),
   d_quantite INTEGER default 0,
   idPortefeuille VARCHAR(50)  NOT NULL,
   idCryptomonnaie VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(idPortefeuille) REFERENCES portefeuille(id),
   FOREIGN KEY(idCryptomonnaie) REFERENCES cryptomonnaie(id)
);

CREATE TABLE historiqueCrypto(
   id VARCHAR(50)  DEFAULT ('HISTO_CRYPTO') || LPAD(NEXTVAL('s_histocrypto')::TEXT, 9, '0'),
   cours NUMERIC(15,2)  ,
   dateChangement TIMESTAMP,
   idCryptomonnaie VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(idCryptomonnaie) REFERENCES cryptomonnaie(id)
);

CREATE TABLE transactionCrypto(
   id VARCHAR(50)  DEFAULT ('HST_TRS') || LPAD(NEXTVAL('s_historique_transaction')::TEXT, 9, '0'),
   dateTransaction TIMESTAMP default current_timestamp,
   quantite INTEGER default 0,
   d_prixUnitaire NUMERIC(15,2)  ,
   d_commission NUMERIC(15,2)   default 0,
   idCryptomonnaie VARCHAR(50)  NOT NULL,
   idAcheteur VARCHAR(14) ,
   idVendeur VARCHAR(14) ,
   PRIMARY KEY(id),
   FOREIGN KEY(idCryptomonnaie) REFERENCES cryptomonnaie(id),
   FOREIGN KEY(idAcheteur) REFERENCES Utilisateur(id),
   FOREIGN KEY(idVendeur) REFERENCES Utilisateur(id)
);

CREATE TABLE commission(
   id VARCHAR(50)  DEFAULT ('CMS') || LPAD(NEXTVAL('s_commission')::TEXT, 9, '0'),
   pourcentage NUMERIC(5,2)   default 0,
   dateChangement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   idCryptomonnaie VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(idCryptomonnaie) REFERENCES cryptomonnaie(id)
);

CREATE TABLE fond(
   id VARCHAR(50)  DEFAULT ('FND') || LPAD(NEXTVAL('s_fond')::TEXT, 9, '0'),
   montant NUMERIC(15,2)  ,
   dateMouvement TIMESTAMP default current_date,
   idTransactionCrypto VARCHAR(50) ,
   idUtilisateur VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(idTransactionCrypto) REFERENCES transactionCrypto(id),
   FOREIGN KEY(idUtilisateur) REFERENCES Utilisateur(id)
);

CREATE TABLE admin(
   id VARCHAR(50)  DEFAULT ('ADM') || LPAD(NEXTVAL('s_admin')::TEXT, 9, '0'),
   nom VARCHAR(50)  NOT NULL,
   mdp VARCHAR(200)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE fondAttente(
   id VARCHAR(50)  DEFAULT ('FND_ATT') || LPAD(NEXTVAL('s_fond_attente')::TEXT, 9, '0'),
   montant NUMERIC(15,2)  ,
   dateMouvement TIMESTAMP default current_date,
   idUtilisateur VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(idUtilisateur) REFERENCES Utilisateur(id)
);

CREATE TABLE cryptofavori(
   idUtilisateur VARCHAR(14) ,
   idCryptomonnaie VARCHAR(50) ,
   PRIMARY KEY(idUtilisateur, idCryptomonnaie),
   FOREIGN KEY(idUtilisateur) REFERENCES Utilisateur(id),
   FOREIGN KEY(idCryptomonnaie) REFERENCES cryptomonnaie(id)
);
