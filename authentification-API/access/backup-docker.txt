# # Utilisation de l'image officielle PHP avec Apache
# FROM php:8.1

# # Installation des extensions nécessaires pour Symfony et PostgreSQL
# RUN apt-get update && apt-get install -y \
#     git zip unzip libpq-dev \
#     && docker-php-ext-install \
#     pdo_pgsql zip

# # Activation du module Apache pour Symfony
# RUN a2enmod rewrite

# # Définir le dossier de travail
# WORKDIR /var/www/symfony

# # Copier le code du projet dans le conteneur
# COPY ./gestion_etudiant /var/www/symfony

# # Installer Composer (et Symfony)
# RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer
# RUN composer install --no-dev --optimize-autoloader

# # Exposer le port HTTP
# EXPOSE 8000


# Utilisation de l'image officielle PHP CLI
FROM php:8.2-cli

# Installer uniquement les dépendances nécessaires pour PostgreSQL et Composer
RUN apt-get update && apt-get install -y git libpq-dev
RUN docker-php-ext-install pdo pdo_pgsql
RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer

# RUN docker-php-ext-install pdo_pgsql 

# Définir le dossier de travail
WORKDIR /var/www/code

# Copier le code du projet dans le conteneur
COPY ./code /var/www/code

RUN COMPOSER_ALLOW_SUPERUSER=1 composer install --no-dev --optimize-autoloader


# Installer Composer (gestionnaire de dépendances PHP)

# Exposer le port 8000 pour HTTP (serveur PHP intégré)
EXPOSE 8000
