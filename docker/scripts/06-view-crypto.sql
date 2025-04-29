\c crypto;

create or replace view 
    v_historique_crypto as 
select h.*, nom, d_valeur from historiquecrypto h 
    join cryptomonnaie c 
on c.id = idcryptomonnaie;

create or replace view 
    v_transaction_crypto as 
select t.id, t.dateTransaction, t.d_commission, t.idcryptomonnaie, c.nom  from transactioncrypto t    
    join cryptomonnaie c
on c.id=t.idcryptomonnaie;

create or replace view 
    v_fondAttenteUtilisateur as 
select f.id, f.montant, f.dateMouvement, f.idUtilisateur, u.nom, u.prenom from fondAttente f 
    inner join utilisateur u 
on f.idUtilisateur = u.id;

create or replace view 
    v_transaction_crypto_utilisateur_crypto as 
select t.*, 
uAcheteur.mail as mailAcheteur, uAcheteur.nom as nomAcheteur, uAcheteur.prenom as prenomAcheteur, uAcheteur.date_naissance as dateNaissanceAcheteur, uAcheteur.lienImage as lienImageAcheteur, 
uVendeur.mail as mailVendeur, uVendeur.nom as nomVendeur, uVendeur.prenom as prenomVendeur, uVendeur.date_naissance as dateNaissanceVendeur, uVendeur.lienImage as lienImageVendeur, 
c.nom as nomCryptomonnaie
    from transactioncrypto t
left join utilisateur uAcheteur on t.idAcheteur=uAcheteur.id  
    left join utilisateur uVendeur on t.idVendeur=uVendeur.id 
left join cryptomonnaie c on t.idcryptomonnaie=c.id; 

set timezone = 'Africa/Nairobi';