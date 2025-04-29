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