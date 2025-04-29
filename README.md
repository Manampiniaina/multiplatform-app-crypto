# PROJET API & CRYPTOMONNAIE 
    ITU P16 

## Membres : 
    . ANDRIAMANANTSILAVO Kiady Manohisoa - ETU002375 
    . RANDRIHANJA Vetso Joella - ETU002589 
    . ARIJESA Manampiniaina - ETU002756
    . ANDRIAMIHAJA Andolalaina Valérie - ETU2386

# Mise en place de l'environnement 
1. Construction et lancement du projet (exécutez la commande suivante) :

    ~ docker-compose up --build -d

2. Collection Postman (Doc-API-Swagger) : 

    ~ Importer le fichier 'Doc-API(Swagger)/swagger.yaml' dans Postman

# URL des applications
1. Fournisseur d'identité : localhost:8000 
2. Cryptomonnaie : localhost:8080
3. Serveur Mail (MailHog) : localhost:8025

# Liens GIT : 
1. Fournisseur d'identité : https://github.com/KiadyManohisoa/Access-API.git
2. Cryptomonnaie : https://github.com/KiadyManohisoa/Crypto-Access.git

# Liens des to do list :
1. Fournisseur d'identité : https://docs.google.com/spreadsheets/d/1cMpAmRjQlqUBozKzLgkxr8MCJY1n-sGwOyvzrt0Gt48/edit?usp=sharing
2. Cryptomonnaie : https://docs.google.com/spreadsheets/d/1iyoyALUOfbgv798f0aWQIMaKiw55uPdgBdwoJKH89uE/edit?usp=sharing

# Remarques : 
1. Après construction et lancement des conteneur, il faut attendre 2 minutes au minimum pour que les données soient parfaitement importés et les conteneurs prêts à l'emploi
2. Pour utiliser d'autres comptes utilisateurs, leur mail et mdp sont placés dans : './data/userData.xlsx'
3. L'inscription et connexion utilisateur nécessitent une validation par email : nécessité du serveur MailHog
4. Le changement des photos de profil nécessite la déconnexion pour être perçue 