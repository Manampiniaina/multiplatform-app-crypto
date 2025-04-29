<?php

    namespace App\Model;

    use Doctrine\DBAL\Connection;
    use Doctrine\DBAL\Exception;

    class Genre
    {
        public string $id;
        public string $libelle;

        // Constructeur
        public function __construct(string $id="", string $libelle="")
        {
            $this->setId($id);
            $this->setLibelle($libelle);
        }

        // Getters et Setters

        public function getId(): string
        {
            return $this->id;
        }

        public function setId(string $id): void
        {
            $this->id = $id;
        }

        public function getLibelle(): string
        {
            return $this->libelle;
        }

        public function setLibelle(string $libelle): void
        {
            $this->libelle = $libelle;
        }

        // Fonctions statiques pour les opérations sur la base de données

        public static function getAll(Connection $connection): array
        {
            $query = 'SELECT * FROM Genre';
            $stmt = $connection->executeQuery($query);
            $results = $stmt->fetchAllAssociative();

            $genres = [];
            foreach ($results as $row) {
                $genres[] = new Genre($row['id'], $row['libelle']);
            }

            return $genres;
        }


        public static function getById(Connection $connection, string $id): ?Genre
        {
            $query = 'SELECT * FROM Genre WHERE id = ?';
            $stmt = $connection->executeQuery($query, [$id]);
            $row = $stmt->fetchAssociative();

            if ($row) {
                return new Genre($row['id'], $row['libelle']);
            }

            return null;
        }

        public function insert(Connection $connection): void
        {
            $query = "INSERT INTO Genre (libelle) VALUES (?) RETURNING id";
            $stmt = $connection->executeQuery($query, [$this->getLibelle()]);
            $id = $stmt->fetchOne();

            $this->setId($id);
        }
    }
?>