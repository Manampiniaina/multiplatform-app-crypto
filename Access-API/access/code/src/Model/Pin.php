<?php

    namespace App\Model;

    use DateTime;
    use App\Service\Utilitaire;

    class Pin
    {
        public ?string $pin;
        public ?DateTime $dateExpiration;
        public ?string $idCompte;

        public function insert($connection): void
        {
            $data = [
                'pin' => $this->getPin(),
                'date_expiration' => $this->getDateExpiration()->format('Y-m-d H:i:s'),  // Formater la date pour l'insertion
                'idcompte' => $this->getIdCompte()  
            ];
            $connection->insert('pin', $data);
        }

        public function __construct(?string $pin='', ?\DateTime $dateExpiration=null, ?string $idCompte='')
        {
            $this->pin = $pin;
            $this->setDateExpiration($dateExpiration);
            $this->idCompte = $idCompte;
        }

        public function getIdCompte():string
        {
            return $this->idCompte;
        }

        public function setIdCompte(string $idCompte): void
        {
            $this->idCompte = $idCompte;
        }

        public function getPin(): ?string
        {
            return $this->pin;
        }

        public function setPin(string $pin): void
        {
            $this->pin = $pin;
        }

        public function getDateExpiration(): ?DateTime
        {
            return $this->dateExpiration;
        }

        public function setDateExpiration(?DateTime $dateExpiration): void
        {
            if($dateExpiration==null) {
                $this->dateExpiration = Utilitaire::getDateActuelle();
            } else          $this->dateExpiration = $dateExpiration;

        }
    }
?>