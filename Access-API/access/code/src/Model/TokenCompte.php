<?php

    namespace App\Model;

    use App\Exception\Model\ValeurInvalideException;
    use App\Exception\Valeur\TokenInvalideException;
    use DateTime;
    use Doctrine\DBAL\Connection;
    use Exception;
    use App\Service\Utilitaire;

    class TokenCompte
    {
        private ?string $id;
        private ?string $valeur;
        private ?string $idCompte;
        private ?DateTime $date_expiration;

        public function getId(): ?string
        {
            return $this->id;
        }

        public function setId(?string $id): void
        {
            $this->id = $id;
        }

        public function getValeur(): ?string
        {
            return $this->valeur;
        }

        public function setValeur(?string $valeur): void
        {
            $this->valeur = $valeur;
        }

        public function getIdCompte(): ?string
        {
            return $this->idCompte;
        }

        public function setIdCompte(?string $idCompte): void
        {
            $this->idCompte = $idCompte;
        }

        public function getDateExpiration(): ?DateTime
        {
            return $this->date_expiration;
        }

        public function setDateExpiration($date_expiration): void
        {
            $this->date_expiration = $date_expiration;
        }

        public function __construct(string $id="", string $valeur="", string $idCompte="", DateTime $date_expiration=null)
        {
            $this->setId($id);
            $this->setValeur($valeur);
            $this->setIdCompte($idCompte);
            $this->setDateExpiration($date_expiration);
        }
        /**
         * Insère le token dans la base de données
         */
        public function insert(Connection $connection): void
        {
            $sql = 'INSERT INTO token_compte ( valeur, date_expiration, idCompte) VALUES (:valeur, :date_expiration, :idCompte)';
            $stmt = $connection->prepare($sql);
            $stmt->bindValue('valeur', $this->getValeur());
            $stmt->bindValue('date_expiration', $this->getDateExpiration()->format('Y-m-d H:i:s'));
            $stmt->bindValue('idCompte', $this->getIdCompte());
            $stmt->executeStatement();
        }

        function estTokenExiste(Connection $connection, string $token) : bool {
            
            $query ="SELECT id FROM token_compte WHERE valeur = ?";
            $stmt = $connection->executeQuery($query, [$token]);

            $resultCompte = $stmt->fetchAssociative();
            if (!$resultCompte ) {
                return true ; 
            }
            // return false ;
            throw new TokenInvalideException(new TokenCompte(valeur:$token), "Token inexistant");
        }

        function estTokenExpire(Connection $connection, string $token) : bool {
            
            $query ="SELECT id FROM token_compte WHERE valeur = ?";
            $stmt = $connection->executeQuery($query, [$token]);
            $resultCompte = $stmt->fetchAssociative();

            $dateActuelle = Utilitaire::getDateActuelle();
            if (!$resultCompte && $dateActuelle<=$resultCompte[' date_expiration']) {
                return true ; 
            }
            // return false ;
            throw new TokenInvalideException(new TokenCompte(valeur:$token), "Token expiré");
        }

        public function estValide(Connection $connection, string $token): bool
        {
            try {
                $this->estTokenExiste($connection, $token);
                $this->estTokenExpire($connection, $token);
                return true;
            }
            catch (Exception $e) {
                throw $e ;
            }
        }
    }

   
?>