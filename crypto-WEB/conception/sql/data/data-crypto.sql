-- Cryptomonnaie 

INSERT INTO cryptomonnaie (id, d_valeur, nom, d_commission) VALUES
('CRYPTO000000001', 2000.00, 'Bitcoin', 25),
('CRYPTO000000002', 1800.50, 'Ethereum', 30),
('CRYPTO000000003', 750.25, 'Cardano', 15),
('CRYPTO000000004', 1200.80, 'Solana', 20),
('CRYPTO000000005', 500.10, 'Dogecoin', 5),
('CRYPTO000000006', 505.50, 'Shiba Inu', 2),
('CRYPTO000000007', 1350.60, 'Polkadot', 18),
('CRYPTO000000008', 980.20, 'Avalanche', 22),
('CRYPTO000000009', 600.75, 'XRP', 12),
('CRYPTO000000010', 725.00, 'Chainlink', 16);


-- Commission : 

INSERT INTO commission (pourcentage, dateChangement, idCryptomonnaie) VALUES
(25.00, NOW(), 'CRYPTO000000001'),
(30.00, NOW(), 'CRYPTO000000002'),
(15.00, NOW(), 'CRYPTO000000003'),
(20.00, NOW(), 'CRYPTO000000004'),
(5.00, NOW(), 'CRYPTO000000005'),
(2.00, NOW(), 'CRYPTO000000006'),
(18.00, NOW(), 'CRYPTO000000007'),
(22.00, NOW(), 'CRYPTO000000008'),
(12.00, NOW(), 'CRYPTO000000009'),
(16.00, NOW(), 'CRYPTO000000010');

INSERT INTO Utilisateur (mail, nom, prenom, date_naissance) VALUES ('kiadymahisoa@gmail.com', 'Andriamanantsilavo', 'Kiady Manohisoa', '2004-05-19');
INSERT INTO Utilisateur (mail, nom, prenom, date_naissance) VALUES ('vetsojoella@gmail.com', 'Randrihanja', 'Vetso Joella', '2003-10-01');

INSERT INTO Portefeuille (idUtilisateur) VALUES ('USR000000001');
INSERT INTO Portefeuille (idUtilisateur) VALUES ('USR000000002');

INSERT INTO fond (montant, dateMouvement, idUtilisateur) VALUES (5000, '2025-02-08 08:00:00', 'USR000000001');