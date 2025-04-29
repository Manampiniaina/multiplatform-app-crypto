<?php

    namespace App\Model;

    use DateTime;
    use Doctrine\DBAL\Connection;
    use App\Service\Utilitaire;

    class Tentative
    {

        public int $nombre;
        public ?DateTime $dateDisponibilite;
        public string $idCompte;

        public function insert(Connection $connection): void
        {
            $idCompte = $this->getIdCompte(); 

            // Insérer dans la table `tentative`
            $connection->insert(
                'tentative',  // Nom de la table
                [
                    'idcompte' => $idCompte,  
                    'date_tentative' => Utilitaire::getDateActuelle()->format('Y-m-d H:i:s'),  
                ]
            );
        }


        public function __construct(int $nombre=0, DateTime $dateDisponibilite=null, string $idCompte = '')
        {
            $this->nombre = $nombre;
            $this->dateDisponibilite = $dateDisponibilite;
            $this->idCompte = $idCompte;
        }

        public function getIdCompte() {
            return $this->idCompte;
        }

        public function setIdCompte(string $idCompte) {
            $this->idCompte = $idCompte;
        }

        
        public function getNombre(): int
        {
            return $this->nombre;
        }

        public function setNombre(int $nombre): void
        {
            $this->nombre = $nombre;
        }

        public function getDateDisponibilite(): ?DateTime
        {
            return $this->dateDisponibilite;
        }

        public function setDateDisponibilite(?DateTime $dateDisponibilite): void
        {
            $this->dateDisponibilite = $dateDisponibilite;
        }

        public function setDateDisponibiliteStr(?string $dateDisponibilite): void
        {
            if($dateDisponibilite==null || $dateDisponibilite=='') {

                $this->dateDisponibilite = null;
                return;
            } 
            $this->setDateDisponibilite( new DateTime($dateDisponibilite)) ;
        }
    }
?>