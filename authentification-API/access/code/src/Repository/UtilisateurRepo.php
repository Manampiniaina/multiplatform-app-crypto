<?php

namespace App\Repository;

use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\DBAL\Exception;
use Doctrine\Persistence\ManagerRegistry;
use Doctrine\ORM\EntityManagerInterface;
use App\Entity\Utilisateur;

class UtilisateurRepo extends ServiceEntityRepository
{
    private $entityManager;

    public function __construct(ManagerRegistry $registry, EntityManagerInterface $entityManager)
    {
        parent::__construct($registry, Utilisateur::class);
        $this->entityManager = $entityManager;
    }

    public function create(string $mail, string $mdp, string $salt, string $nom, ?string $prenom, \DateTime $dateNaissance, string $idGenre): Utilisateur
    {
        if ($this->emailExists($mail)) {
            throw new \InvalidArgumentException('L\'email est déjà utilisé.');
        }

        $newId = $this->getNewId();

        $utilisateur = new Utilisateur();
        $utilisateur->setId($newId);
        $utilisateur->setMail($mail);
        $utilisateur->setMdp($mdp);
        $utilisateur->setSalt($salt);
        $utilisateur->setNom($nom);
        $utilisateur->setPrenom($prenom);
        $utilisateur->setDateNaissance($dateNaissance);
        $utilisateur->setIdGenre($idGenre);

        $this->entityManager->persist($utilisateur);
        $this->entityManager->flush();

        return $utilisateur;
    }
    public function insertWithDefaultValues(Utilisateur $user): Utilisateur
    {
        $query = '
            INSERT INTO utilisateur (id, mail, mdp, salt, nom, prenom, date_naissance, idgenre)
            VALUES (DEFAULT, :mail, :mdp, :salt, :nom, :prenom, :date_naissance, :idgenre)
            RETURNING id
        ';
        $conn = $this->entityManager->getConnection();

        try {
            $stmt = $conn->prepare($query);

            $stmt->bindValue('mail', $user->getMail());
            $stmt->bindValue('mdp', $user->getMdp());
            $stmt->bindValue('salt', $user->getSalt());
            $stmt->bindValue('nom', $user->getNom());
            $stmt->bindValue('prenom', $user->getPrenom());
            $stmt->bindValue('date_naissance', $user->getDateNaissance()->format('Y-m-d'));
            $stmt->bindValue('idgenre', $user->getIdGenre());

            $stmt->executeQuery();
            return $user;

        } catch (Exception $e) {
            throw new Exception($e->getMessage());
        }

    }

    private function emailExists(string $email): bool
    {
        $existsInDb = (bool) $this->createQueryBuilder('u')
            ->select('COUNT(u.id)')
            ->where('u.mail = :email')
            ->setParameter('email', $email)
            ->getQuery()
            ->getSingleScalarResult();

        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            throw new \InvalidArgumentException('L\'email n\'est pas valide.');
        }

        return $existsInDb;
    }
}
