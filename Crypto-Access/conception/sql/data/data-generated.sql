INSERT INTO admin (nom, mdp) VALUES
('Rakoto', MD5('rakoto')),
('Rasoamanana', MD5('Rasoamanana'));

INSERT INTO cryptomonnaie (id, d_valeur, nom, d_commission) VALUES
('CRYPTO000000001', 2000.00, 'Bitcoin', 5.00),
('CRYPTO000000002', 1800.50, 'Ethereum', 5.00),
('CRYPTO000000003', 750.25, 'Cardano', 5.00),
('CRYPTO000000004', 1200.80, 'Solana', 5.00),
('CRYPTO000000005', 500.10, 'Dogecoin',5.00),
('CRYPTO000000006', 505.50, 'Shiba Inu',5.00),
('CRYPTO000000007', 1350.60, 'Polkadot', 5.00),
('CRYPTO000000008', 980.20, 'Avalanche', 5.00),
('CRYPTO000000009', 600.75, 'XRP', 5.00),
('CRYPTO000000010', 725.00, 'Chainlink', 5.00);

INSERT INTO utilisateur (mail, nom, prenom, date_naissance, lienImage) VALUES ('leadupuis@gmail.com', 'DUPUIS', 'L├®a', '1992-03-15', NULL);
INSERT INTO utilisateur (mail, nom, prenom, date_naissance, lienImage) VALUES ('lucmartin@hotmail.com', 'MARTIN', 'Luc', '1988-07-02', NULL);
INSERT INTO utilisateur (mail, nom, prenom, date_naissance, lienImage) VALUES ('sophiebernard@gmail.com', 'BERNARD', 'Sophie', '1995-11-29', NULL);
INSERT INTO utilisateur (mail, nom, prenom, date_naissance, lienImage) VALUES ('marcrobert@gmail.com', 'ROBERT', 'Marc', '1990-06-18', NULL);
INSERT INTO utilisateur (mail, nom, prenom, date_naissance, lienImage) VALUES ('emiliedurand@ymail.com', 'DURAND', '├ëmilie', '1987-09-05', NULL);
INSERT INTO utilisateur (mail, nom, prenom, date_naissance, lienImage) VALUES ('pierreleclerc@ymail.com', 'LECLERC', 'Pierre', '1993-04-22', NULL);
INSERT INTO utilisateur (mail, nom, prenom, date_naissance, lienImage) VALUES ('claireleroy@ymail.com', 'LEROY', 'Claire', '1989-12-10', NULL);
INSERT INTO utilisateur (mail, nom, prenom, date_naissance, lienImage) VALUES ('nicolasandre@gmail.com', 'ANDR├ë', 'Nicolas', '1991-02-08', NULL);
INSERT INTO utilisateur (mail, nom, prenom, date_naissance, lienImage) VALUES ('alicemoreau@hotmail.com', 'MOREAU', 'Alice', '1994-08-25', NULL);
INSERT INTO utilisateur (mail, nom, prenom, date_naissance, lienImage) VALUES ('antoinesimon@ymail.com', 'SIMON', 'Antoine', '1986-01-14', NULL);

INSERT INTO portefeuille (idutilisateur) VALUES ('USR000000001');
INSERT INTO portefeuille (idutilisateur) VALUES ('USR000000002');
INSERT INTO portefeuille (idutilisateur) VALUES ('USR000000003');
INSERT INTO portefeuille (idutilisateur) VALUES ('USR000000004');
INSERT INTO portefeuille (idutilisateur) VALUES ('USR000000005');
INSERT INTO portefeuille (idutilisateur) VALUES ('USR000000006');
INSERT INTO portefeuille (idutilisateur) VALUES ('USR000000007');
INSERT INTO portefeuille (idutilisateur) VALUES ('USR000000008');
INSERT INTO portefeuille (idutilisateur) VALUES ('USR000000009');
INSERT INTO portefeuille (idutilisateur) VALUES ('USR000000010');


-- Commission : 

INSERT INTO commission (pourcentage, dateChangement, idCryptomonnaie) VALUES
(5.00, NOW(), 'CRYPTO000000001'),
(5.00, NOW(), 'CRYPTO000000002'),
(5.00, NOW(), 'CRYPTO000000003'),
(5.00, NOW(), 'CRYPTO000000004'),
(5.00, NOW(), 'CRYPTO000000005'),
(5.00, NOW(), 'CRYPTO000000006'),
(5.00, NOW(), 'CRYPTO000000007'),
(5.00, NOW(), 'CRYPTO000000008'),
(5.00, NOW(), 'CRYPTO000000009'),
(5.00, NOW(), 'CRYPTO000000010');

-- Insertion des données pour la table historiqueCrypto
INSERT INTO historiqueCrypto (cours, datechangement, idcryptomonnaie)
SELECT 
    (500 + random() * 1500)::numeric(15,2), -- Cours aléatoire entre 500 et 2000
    timestamp '2025-02-07' + (n || ' hours')::interval, -- Date de changement
    'CRYPTO00000000' || (i::text) -- ID de la cryptomonnaie
FROM generate_series(1, 10) AS i, -- 10 cryptomonnaies
     generate_series(0, 23, 2.4) AS n; -- 10 changements par jour

-- Répéter pour les autres jours
INSERT INTO historiqueCrypto (cours, datechangement, idcryptomonnaie)
SELECT 
    (500 + random() * 1500)::numeric(15,2),
    timestamp '2025-02-08' + (n || ' hours')::interval,
    'CRYPTO00000000' || (i::text)
FROM generate_series(1, 10) AS i,
     generate_series(0, 23, 2.4) AS n;

INSERT INTO historiqueCrypto (cours, datechangement, idcryptomonnaie)
SELECT 
    (500 + random() * 1500)::numeric(15,2),
    timestamp '2025-02-09' + (n || ' hours')::interval,
    'CRYPTO00000000' || (i::text)
FROM generate_series(1, 10) AS i,
     generate_series(0, 23, 2.4) AS n;

     -- Insertion des fonds initiaux pour les utilisateurs
INSERT INTO fond (montant, datemouvement, idutilisateur)
SELECT 
    10000.00, -- Montant fixe de 10 000 €
    '2025-02-07', -- Date de mouvement
    'USR00000000' || (i::text) -- ID de l'utilisateur
FROM generate_series(1, 10) AS i;

-- Insertion des achats
WITH prix_crypto AS (
    SELECT 
        idcryptomonnaie,
        cours,
        datechangement,
        ROW_NUMBER() OVER (PARTITION BY idcryptomonnaie ORDER BY ABS(datechangement - date_transaction)) AS rn
    FROM historiqueCrypto
    CROSS JOIN (SELECT timestamp '2025-02-07' + (random() * interval '3 days') AS date_transaction) AS dt
    WHERE idcryptomonnaie = 'CRYPTO00000001'
)
INSERT INTO transactionCrypto (datetransaction, quantite, d_prixunitaire, d_commission, idcryptomonnaie, idacheteur, idvendeur)
SELECT 
    date_transaction, -- Date générée aléatoirement
    5, -- Quantité fixe de 5
    cours, -- Prix unitaire basé sur le cours le plus proche
    (5 * cours * 0.05)::numeric(15,2), -- Commission de 5%
    'CRYPTO00000001', -- ID de la cryptomonnaie fixe
    'USR00000001', -- ID de l'acheteur fixe
    NULL -- ID du vendeur (NULL car c'est la plateforme qui vend)
FROM prix_crypto
WHERE rn = 1
LIMIT 10; -- 10 achats

-- Insertion des ventes
WITH prix_crypto AS (
    SELECT 
        idcryptomonnaie,
        cours,
        datechangement,
        ROW_NUMBER() OVER (PARTITION BY idcryptomonnaie ORDER BY ABS(datechangement - date_transaction)) AS rn
    FROM historiqueCrypto
    CROSS JOIN (SELECT timestamp '2025-02-07' + (random() * interval '3 days') AS date_transaction) AS dt
    WHERE idcryptomonnaie = 'CRYPTO00000002'
)
INSERT INTO transactionCrypto (datetransaction, quantite, d_prixunitaire, d_commission, idcryptomonnaie, idacheteur, idvendeur)
SELECT 
    date_transaction, -- Date générée aléatoirement
    3, -- Quantité fixe de 3
    cours, -- Prix unitaire basé sur le cours le plus proche
    (3 * cours * 0.05)::numeric(15,2), -- Commission de 5%
    'CRYPTO00000002', -- ID de la cryptomonnaie fixe
    NULL, -- ID de l'acheteur (NULL car c'est la plateforme qui achète)
    'USR00000002' -- ID du vendeur fixe
FROM prix_crypto
WHERE rn = 1
LIMIT 7; -- 7 ventes

-- Mise à jour de portefeuille_detail après achats
WITH achat AS (
    SELECT idacheteur, idcryptomonnaie, quantite
    FROM transactionCrypto
    WHERE idacheteur IS NOT NULL
)
INSERT INTO portefeuille_detail (d_quantite, idportefeuille, idcryptomonnaie)
SELECT 
    achat.quantite,
    portefeuille.id,
    achat.idcryptomonnaie
FROM achat
JOIN portefeuille ON portefeuille.idutilisateur = achat.idacheteur
ON CONFLICT (idportefeuille, idcryptomonnaie) DO UPDATE
SET d_quantite = portefeuille_detail.d_quantite + EXCLUDED.d_quantite;

-- Mise à jour de portefeuille_detail après ventes
WITH vente AS (
    SELECT idvendeur, idcryptomonnaie, quantite
    FROM transactionCrypto
    WHERE idvendeur IS NOT NULL
)
UPDATE portefeuille_detail
SET d_quantite = d_quantite - vente.quantite
FROM vente
JOIN portefeuille ON portefeuille.idutilisateur = vente.idvendeur
WHERE portefeuille_detail.idportefeuille = portefeuille.id
AND portefeuille_detail.idcryptomonnaie = vente.idcryptomonnaie;

-- Mise à jour des fonds après achats
WITH achat AS (
    SELECT idacheteur, (quantite * d_prixunitaire + d_commission) AS montant
    FROM transactionCrypto
    WHERE idacheteur IS NOT NULL
)
UPDATE fond
SET montant = montant - achat.montant
FROM achat
WHERE fond.idutilisateur = achat.idacheteur;

-- Mise à jour des fonds après ventes
WITH vente AS (
    SELECT idvendeur, (quantite * d_prixunitaire - d_commission) AS montant
    FROM transactionCrypto
    WHERE idvendeur IS NOT NULL
)
UPDATE fond
SET montant = montant + vente.montant
FROM vente
WHERE fond.idutilisateur = vente.idvendeur;