CREATE OR REPLACE VIEW v_CompteUtilisateur AS 
    SELECT u.mail, u.mdp, u.salt, c.* FROM Compte c INNER JOIN Utilisateur u
ON c.id_utilisateur = u.id;


set timezone = 'Africa/Nairobi';