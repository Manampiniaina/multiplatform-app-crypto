# Access-API

Projet API (ITU P16) facilitant la gestion des identités utilisateurs

## Commande à lancer 
1. Se mettre dans le dossier API project
2. Lancer la commande : **docker-compose up -d**
3. Vérifier l'état des serveurs : base de données et l'app : **docker ps** 
4. Test des application via postman 

## A considérer 
1. Accéder à votre base de données dans docker avec : **psql -U api -d accessapi -h localhost -P 5433**
2. Il faudra encore copier le script de la table contenu dans _script/table.sql_ et les vues dans _view.sql_ 
