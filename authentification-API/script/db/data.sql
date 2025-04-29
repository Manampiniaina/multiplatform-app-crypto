INSERT INTO Genre (libelle) VALUES ('homme'), ('femme');

INSERT INTO Utilisateur (mail, mdp, nom, prenom, date_naissance, idGenre) VALUES 
('jeanaime@gmail.com', 'aeae12jj', 'Rasolo', 'Jean Aimé', '2004-11-18', 'GR01');

INSERT INTO Utilisateur (mail, mdp, nom, prenom, date_naissance, idGenre) VALUES 
('michelronsard@gmail.com', 'aegae', 'Michel', 'Ronsard', '2002-02-11', 'GR02');

INSERT INTO Compte (d_date_debloquage, d_pin_actuel, d_date_expiration_pin, id_Utilisateur) 
VALUES (null, null, null, 'USR000000001');