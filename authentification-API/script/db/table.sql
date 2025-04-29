CREATE TABLE Genre(
   id VARCHAR(14)  DEFAULT ('GR') || LPAD(NEXTVAL('s_Genre')::TEXT, 2, '0'),
   libelle VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(libelle)
);

CREATE TABLE Utilisateur(
   id VARCHAR(14)  DEFAULT ('USR') || LPAD(NEXTVAL('s_Utilisateur')::TEXT, 9, '0'),
   mail VARCHAR(50)  NOT NULL,
   mdp VARCHAR(200)  NOT NULL,
   nom VARCHAR(100)  NOT NULL,
   prenom VARCHAR(30) ,
   date_naissance DATE NOT NULL,
   idGenre VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(mail),
   FOREIGN KEY(idGenre) REFERENCES Genre(id)
);

CREATE TABLE Compte(
   id VARCHAR(14)  DEFAULT ('CMT') || LPAD(NEXTVAL('s_Compte')::TEXT, 9, '0'),
   d_nb_tentative SMALLINT DEFAULT 0,
   d_date_debloquage TIMESTAMP,
   d_pin_actuel VARCHAR(6) ,
   d_date_expiration_pin TIMESTAMP,
   id_Utilisateur VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id_Utilisateur),
   FOREIGN KEY(id_Utilisateur) REFERENCES Utilisateur(id)
);

CREATE TABLE Tentative(
   id VARCHAR(14)  DEFAULT ('TTV') || LPAD(NEXTVAL('s_Tentative')::TEXT, 9, '0'),
   date_tentative TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   idCompte VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(idCompte) REFERENCES Compte(id)
);

CREATE TABLE Token_compte(
   id VARCHAR(14)  DEFAULT ('TKN') || LPAD(NEXTVAL('s_Token')::TEXT, 9, '0'),
   date_expiration TIMESTAMP NOT NULL,
   valeur VARCHAR(200)  NOT NULL,
   idCompte VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(idCompte) REFERENCES Compte(id)
);

CREATE TABLE PIN(
   id VARCHAR(14)  DEFAULT ('PIN') || LPAD(NEXTVAL('s_Pin')::TEXT, 9, '0'),
   pin VARCHAR(6)  NOT NULL,
   date_expiration TIMESTAMP NOT NULL,
   idCompte VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(idCompte) REFERENCES Compte(id)
);


-- alter table Utilisateur add column salt varchar(250);