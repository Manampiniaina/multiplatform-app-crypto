<?php
namespace App\Service;

use Doctrine\DBAL\Connection;
use Doctrine\DBAL\Driver\Statement;

class UtilDAO
{
    /**
     * Génère une clé primaire basée sur le nombre d'entrées dans une table.
     *
     * @param Connection $conn La connexion à la base de données.
     * @param string $table Le nom de la table.
     * @param string $idPrefix Le préfixe de l'ID.
     * @return string Le nouvel identifiant généré.
     * @throws \Doctrine\DBAL\Exception
     */
    // Dans votre méthode UtileDAO
public static function construirePK(Connection $conn, string $table, string $idPrefix): string
{
    $query = "SELECT COUNT(*) FROM " . $table;
    $stmt = $conn->executeQuery($query);

    // Utilisation de fetchOne() pour récupérer le résultat
    $count = $stmt->fetchOne();

    // Incrémentation du compteur
    return sprintf("%s%02d", $idPrefix, $count + 1);
}

}
