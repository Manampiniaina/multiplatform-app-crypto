# 🛠️ Étape 1 : Construction et récupération des dépendances Maven (cachées entre les builds)
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copier uniquement le fichier pom.xml pour télécharger les dépendances
COPY /crypto/pom.xml .

# Télécharger les dépendances Maven (cette étape est cachée si elles sont déjà dans le cache)
RUN mvn dependency:go-offline

# Maintenant, copier le reste des fichiers sources
COPY /crypto/src/ ./src

# 🏃‍♂️ Étape 2 : Exécution de l'application Spring Boot en mode développement avec Maven
CMD ["mvn", "spring-boot:run"]
