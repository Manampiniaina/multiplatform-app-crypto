\c accessapi;

CREATE TABLE genre(
   id VARCHAR(14)  DEFAULT ('GR') || LPAD(NEXTVAL('s_genre')::TEXT, 2, '0'),
   libelle VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(libelle)
);

CREATE TABLE utilisateur(
   id VARCHAR(14)  DEFAULT ('USR') || LPAD(NEXTVAL('s_utilisateur')::TEXT, 9, '0'),
   mail VARCHAR(50)  NOT NULL,
   mdp VARCHAR(200)  NOT NULL,
   salt VARCHAR(250)  NOT NULL,
   nom VARCHAR(100)  NOT NULL,
   prenom VARCHAR(30) ,
   date_naissance DATE NOT NULL,
   idgenre VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(mail),
   FOREIGN KEY(idgenre) REFERENCES genre(id)
);

CREATE TABLE compte(
   id VARCHAR(14)  DEFAULT ('CMT') || LPAD(NEXTVAL('s_compte')::TEXT, 9, '0'),
   d_nb_tentative SMALLINT DEFAULT 0,
   d_date_debloquage TIMESTAMP,
   d_pin_actuel VARCHAR(6) ,
   d_date_expiration_pin TIMESTAMP,
   id_utilisateur VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id_utilisateur),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id)
);

CREATE TABLE Tentative(
   id VARCHAR(14)  DEFAULT ('TTV') || LPAD(NEXTVAL('s_tentative')::TEXT, 9, '0'),
   date_tentative TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   idcompte VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(idcompte) REFERENCES compte(id)
);

CREATE TABLE Token_compte(
   id VARCHAR(14)  DEFAULT ('TKN') || LPAD(NEXTVAL('s_token')::TEXT, 9, '0'),
   date_expiration TIMESTAMP NOT NULL,
   valeur VARCHAR(200)  NOT NULL,
   idcompte VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(idcompte) REFERENCES compte(id)
);

CREATE TABLE PIN(
   id VARCHAR(14)  DEFAULT ('PIN') || LPAD(NEXTVAL('s_pin')::TEXT, 9, '0'),
   pin VARCHAR(6)  NOT NULL,
   date_expiration TIMESTAMP NOT NULL,
   idcompte VARCHAR(14)  NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(idcompte) REFERENCES compte(id)
);