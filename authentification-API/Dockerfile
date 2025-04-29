# Utilisation de l'image officielle PHP CLI
FROM php:8.2-cli

# Installer les dépendances nécessaires pour PostgreSQL, Composer, zip et unzip
RUN apt-get update && apt-get install -y \
    git \
    libpq-dev \
    zip \
    unzip \
    && docker-php-ext-install pdo pdo_pgsql

# Installer Composer (gestionnaire de dépendances PHP)
RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer

# Définir le dossier de travail
WORKDIR /var/www/access

# Copier uniquement les fichiers nécessaires pour installer les dépendances
COPY access/code/composer.json access/code/composer.lock ./

# Installer les dépendances du projet (sans les dépendances de développement)
RUN COMPOSER_ALLOW_SUPERUSER=1 composer install --no-dev --optimize-autoloader --no-scripts

# Copier le reste du code (sauf le dossier vendor qui sera géré au runtime)
COPY access/code .

# Exposer le port 8000
EXPOSE 8000

# Assurer que les dépendances sont bien installées avant de lancer le serveur
CMD composer install --no-dev --optimize-autoloader --no-scripts && php -S 0.0.0.0:8000 -t public
