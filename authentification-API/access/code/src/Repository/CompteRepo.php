<?php

namespace App\Repository;

use App\Entity\Compte;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\DBAL\Exception;
use Doctrine\Persistence\ManagerRegistry;
use Doctrine\ORM\EntityManagerInterface;
use App\Entity\Utilisateur;

class CompteRepo extends ServiceEntityRepository
{
    private $entityManager;

    public function __construct(ManagerRegistry $registry, EntityManagerInterface $entityManager)
    {
        parent::__construct($registry, Compte::class);
        $this->entityManager = $entityManager;
    }

    public function create(Compte $obj): Compte
    {
        $query = '
            INSERT INTO compte (id , d_nb_tentative , d_date_debloquage , d_pin_actuel  , d_date_expiration_pin ,id_Utilisateur)
            VALUES (DEFAULT, :d_nb_tentative, :d_date_debloquage, :d_pin_actuel, :d_date_expiration_pin, :id_Utilisateur)
        ';
        $conn = $this->entityManager->getConnection();

        try {
            $stmt = $conn->prepare($query);

            $stmt->bindValue('d_nb_tentative', $obj->getDNbTentative());
            $stmt->bindValue('d_date_debloquage', $obj->getDDateDebloquage());
            $stmt->bindValue('d_pin_actuel', $obj->getDPinActuel());
            $stmt->bindValue('d_date_expiration_pin', $obj->getDDateExpirationPin());
            $stmt->bindValue('id_Utilisateur', $obj->getIdUtilisateur() );

            $stmt->executeQuery();
            return $obj;

        } catch (Exception $e) {
            throw new Exception($e->getMessage());
        }

    }

}
