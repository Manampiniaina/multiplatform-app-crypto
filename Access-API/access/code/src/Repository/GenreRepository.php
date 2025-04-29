<?php 
// src/Repository/GenreRepository.php

    namespace App\Repository;

    use App\Entity\Genre;
    use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
    use Doctrine\Persistence\ManagerRegistry;

    
    class GenreRepository extends ServiceEntityRepository
    {
        public function __construct(ManagerRegistry $registry)
        {
            parent::__construct($registry, Genre::class);
        }

        // Exemple de méthode personnalisée pour récupérer tous les genres
        public function getAll():array
        {
            return $this->findAll();
        }

        // Exemple de méthode personnalisée pour récupérer un genre par son ID
        public function getById($id)
        {
            return $this->find($id);
        }
    }
?>