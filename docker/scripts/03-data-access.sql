\c accessapi;

SET client_encoding = 'UTF8';

INSERT INTO genre (libelle) VALUES ('homme');
INSERT INTO genre (libelle) VALUES ('femme');

INSERT INTO Utilisateur (mail, mdp, salt, nom, prenom, date_naissance, idgenre) VALUES
('leadupuis@gmail.com', '$2y$10$s9o0v1Ys/CxGlEWWISGwF.jDkKOLUuBct67lxXep7PF9FdssWkg8m', 'z[JoYD@iTMLp8ujA', 'DUPUIS', 'Léa', '1992-03-15', 'GR02'),
('lucmartin@hotmail.com', '$2y$10$JcSMVATcJLGJ/DlMZZ1JTeGHoENv3q9WgstX1ISTyuEdd4NOmy0Qe', 'Qg6&Who3p@P.QU!@', 'MARTIN', 'Luc', '1988-07-02', 'GR01'),
('sophiebernard@gmail.com', '$2y$10$l5iPtdRA2GHB0qj3cWHFd.Z8fNiQ7HybLmEKoHKTV8/wnx17HYsrq', '11bu).J9qrs&_q4A', 'BERNARD', 'Sophie', '1995-11-29', 'GR02'),
('marcrobert@gmail.com', '$2y$10$DGt8ah0dfMCw05xpaXvNGuFzCd.Fm/i5OuVIEKYC7aEf/Z92yW3Re', '+FS1,XCLImI+hhdF', 'ROBERT', 'Marc', '1990-06-18', 'GR01'),
('emiliedurand@ymail.com', '$2y$10$6QbqNUUqYF40MIz1e3myE.P0Qd2BPGxqI95bYbtWToVRb0uoJiEWa', 'SDG-/[xT]JbXkTG;', 'DURAND', 'Émilie', '1987-09-05', 'GR02'),
('pierreleclerc@ymail.com', '$2y$10$WplpueoFyc92yOmYjQQNpOtcUJgXPJEzc416nU5FmiLxOufphvX2i', 'o[<Blw1vCTK$.lRO', 'LECLERC', 'Pierre', '1993-04-22', 'GR01'),
('claireleroy@ymail.com', '$2y$10$gD8uzYIPwcLgnGTCY.UstuExHwxeJo5HdkpxAKFhRZkaRkHXRQ.Ny', '?YI87c_n<taCv9=y', 'LEROY', 'Claire', '1989-12-10', 'GR02'),
('nicolasandre@gmail.com', '$2y$10$SJ22IqTwm/h6j7bJComB7uHBtZRoJH5XDotSNXomeOW29gs63KFRS', 'laYSrPRkg<c?=gz+', 'ANDRÉ', 'Nicolas', '1991-02-08', 'GR01'),
('alicemoreau@hotmail.com', '$2y$10$QVIowif9/U4SavIGPqVEEOmAF8q1ShcnfpCDnfdgQ1oWEkzh6lb26', '*zNtccUMX4YcLMzg', 'MOREAU', 'Alice', '1994-08-25', 'GR02'),
('antoinesimon@ymail.com', '$2y$10$oQYHOUi1N7U2XFu1ioMu6.rrUegZbgHlhm8hlp96tvRTZBAvkuCuC', 'x&7pwEyKZfAvW:<&', 'SIMON', 'Antoine', '1986-01-14', 'GR01');

INSERT INTO compte (d_nb_tentative, d_date_debloquage, d_pin_actuel, d_date_expiration_pin, id_utilisateur) VALUES
(0, NULL, NULL, NULL, 'USR000000006'),
(0, NULL, NULL, NULL, 'USR000000007'),
(0, NULL, NULL, NULL, 'USR000000008'),
(0, NULL, NULL, NULL, 'USR000000009'),
(0, NULL, NULL, NULL, 'USR000000010'),
(0, NULL, 1153, '2025-02-10 04:36:07', 'USR000000002'),
(0, NULL, 5402, '2025-02-10 04:40:10', 'USR000000003'),
(0, NULL, 9374, '2025-02-10 04:44:29', 'USR000000004'),
(0, NULL, 6803, '2025-02-10 04:45:39', 'USR000000005'),
(0, NULL, 9001, '2025-02-10 06:45:11', 'USR000000001');

INSERT INTO pin (pin, date_expiration, idcompte) VALUES
(3142, '2025-02-10 04:07:32', 'CMT000000001'),
(6069, '2025-02-10 04:09:14', 'CMT000000002'),
(4837, '2025-02-10 04:09:53', 'CMT000000003'),
(7372, '2025-02-10 04:10:38', 'CMT000000004'),
(7235, '2025-02-10 04:11:15', 'CMT000000005'),
(2248, '2025-02-10 04:12:07', 'CMT000000001'),
(9478, '2025-02-10 04:27:16', 'CMT000000001'),
(0567, '2025-02-10 04:31:53', 'CMT000000001'),
(1153, '2025-02-10 04:36:07', 'CMT000000002'),
(5402, '2025-02-10 04:40:10', 'CMT000000003'),
(9374, '2025-02-10 04:44:29', 'CMT000000004'),
(6803, '2025-02-10 04:45:39', 'CMT000000005'),
(4126, '2025-02-10 04:47:12', 'CMT000000001'),
(4543, '2025-02-10 05:02:52', 'CMT000000001'),
(7124, '2025-02-10 05:10:57', 'CMT000000001'),
(9001, '2025-02-10 06:45:11', 'CMT000000001');

INSERT INTO tentative (date_tentative, idcompte) VALUES
('2025-02-10 04:05:45', 'CMT000000001');

INSERT INTO token_compte (date_expiration, valeur, idcompte) VALUES
('2025-02-11 04:06:27', '2d159ee27dd72221f5c8e3d293574056', 'CMT000000001'),
('2025-02-11 04:07:25', '4348f547510b8c8ff6ace7649552c319', 'CMT000000002'),
('2025-02-11 04:08:05', '51f09f41ea2246cd522eff34d348eb7e', 'CMT000000003'),
('2025-02-11 04:08:48', 'a2ae6bcdef161900fb39fd2dd02bc1e9', 'CMT000000004'),
('2025-02-11 04:09:25', '16a9b47bfc6c942c8b25bd757b11a01b', 'CMT000000005'),
('2025-02-11 04:10:16', '6fe26bf4d01bbb121071cc6f0f126e3b', 'CMT000000001'),
('2025-02-11 04:25:29', 'c87b87723229fd53b4a69b8fd8ff0dab', 'CMT000000001'),
('2025-02-11 04:30:08', '985b9ca1cacba6427df210f30d8c6f23', 'CMT000000001'),
('2025-02-11 04:34:16', 'fe01ae1141959bf6d34c7f0d70cbef4a', 'CMT000000002'),
('2025-02-11 04:38:23', 'fde70f7c87c02e4881a0d955ec94615e', 'CMT000000003'),
('2025-02-11 04:42:41', '3e1f94cfd176fc2ff070d9ca881a713a', 'CMT000000004'),
('2025-02-11 04:43:52', '10e4bde7433e011c9f7f657bd4baacbb', 'CMT000000005'),
('2025-02-11 04:45:22', '10a3f4378c9e772bf0883438434d9102', 'CMT000000001'),
('2025-02-11 05:01:19', '68dbda58a68bd113e8466b92d6870da7', 'CMT000000001'),
('2025-02-11 05:09:49', '23ab6cd384007453bc1f9bb27e612c41', 'CMT000000001'),
('2025-02-11 06:43:23', 'a01e846014a325a2900eac48705f27ff', 'CMT000000001');

