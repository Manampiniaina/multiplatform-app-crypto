<?php

    namespace App\Model;

    use App\Config\AppConfig;
    use App\Model\Utilisateur;
    use App\Model\Tentative;
    use App\Model\Pin;
    use App\Service\Utilitaire;
    use Doctrine\DBAL\Connection;
    use DateTime;
    use DateInterval;
    use App\Exception\Model\ModelException;
    use App\Exception\Model\CompteException;


    class Compte
    {
        public string $id;
        public Utilisateur $utilisateur;
        public Tentative $tentative;
        public Pin $pin;

        public function estCompteBloque($connection) : void {
            $dateActuelle = Utilitaire::getDateActuelle();
            $dateDispo = $this->getTentative()->getDateDisponibilite();
            if($dateDispo!=null) {
                if($dateActuelle <= $dateDispo) {
                    throw new CompteException("Le compte est temporairement bloqué jusqu'au {$dateDispo->format('Y-m-d H:i:s')}");
                } else {
                    $tentative = new Tentative(nombre:0, dateDisponibilite:null);
                    $this->setTentative($tentative);
                    $this->update($connection);
                }
            }
        }

        public function update(Connection $connection): void
        {
            $data = [
                'd_nb_tentative' => $this->getTentative()->getNombre(),
                'd_date_debloquage' => $this->getTentative()->getDateDisponibilite() ? $this->getTentative()->getDateDisponibilite()->format('Y-m-d H:i:s') : null,
            ];

            $connection->update(
                'compte',  
                $data,  
                ['id' => $this->getId()]
            );
        }

        public function updatePIN(Connection $connection): void
        {
            $data = [
                'd_pin_actuel' => $this->getPin()->getPin(),
                'd_date_expiration_pin' => $this->getPin()->getDateExpiration() ? $this->getPin()->getDateExpiration()->format('Y-m-d H:i:s') : null
            ];

            $connection->update(
                'compte',  
                $data,  
                ['id' => $this->getId()]
            );
        }

        public function ajouterTentativeEchouer(Connection $connection) : void {
            $this->getTentative()->setIdCompte($this->getId());
            $this->getTentative()->insert($connection);
            $this->getTentative()->setNombre($this->getTentative()->getNombre()+1);
            $this->getTentative()->setDateDisponibilite(null);
            $this->update($connection);
        }
        
        public function bloquer(Connection $connection): void
        {
            $this->getTentative()->setIdCompte($this->getId());
            $this->getTentative()->insert($connection);
            
            $compteId = $this->id;
            $this->getTentative()->setNombre($this->getTentative()->getNombre() + 1);
            $utilitaire = new Utilitaire();
            $connection->update(
                'compte', // Nom de la table
                [
                    'd_nb_tentative' => $this->getTentative()->getNombre() ,  
                    'd_date_debloquage' => $utilitaire->getDatePrevuee(AppConfig::DUREE_BLOQUAGE_COMPTE)->format('Y-m-d H:i:s')
                ],
                [
                    'id' => $this->getId()
                ]
            );
        }

        public function __construct(string $id='', Utilisateur $utilisateur= new Utilisateur(), Tentative $tentative=new Tentative(), Pin $pin=new Pin())
        {
            $this->id = $id;
            $this->utilisateur = $utilisateur;
            $this->tentative = $tentative;
            $this->pin = $pin;
        }

        public function setCompteByMail(Connection $connection): void
        {
            $firstQuery = "SELECT * FROM v_CompteUtilisateur WHERE mail = ?";
            try {
                $result = $connection->fetchAssociative($firstQuery, [$this->getUtilisateur()->getMail()]);
                $this->setId($result['id']);
                $this->setUtilisateur(new Utilisateur(mail:$result['mail'], mdp:$result['mdp']));
                $this->setTentative(new Tentative((int)$result['d_nb_tentative'], new DateTime($result['d_date_debloquage'])));
                $this->setPin(new Pin($result['d_pin_actuel'], new DateTime($result['d_date_expiration_pin'])));
            } catch (\Exception $e) {
                throw $e;
            }


        }

        public function getId(): string
        {
            return $this->id;
        }

        public function setId(string $id): void
        {
            $this->id = $id;
        }

        public function getUtilisateur(): Utilisateur
        {
            return $this->utilisateur;
        }

        public function setUtilisateur(Utilisateur $utilisateur): void
        {
            $this->utilisateur = $utilisateur;
        }

        public function getTentative(): Tentative
        {
            return $this->tentative;
        }

        public function setTentative(Tentative $tentative): void
        {
            $this->tentative = $tentative;
        }

        public function getPin(): Pin
        {
            return $this->pin;
        }

        public function setPin(Pin $pin): void
        {
            $this->pin = $pin;
        }

        public static function getById(Connection $connection, string $id):?Compte{
            
            $query = 'SELECT * FROM Compte WHERE id = ?';
            $stmt = $connection->executeQuery($query, [$id]);
            $row = $stmt->fetchAssociative();

            if ($row) {
                $utilisateur = Utilisateur::getById($connection, $row['id_utilisateur']);
                $pin = new Pin(pin:$row['d_pin_actuel'], dateExpiration:new DateTime($row['d_date_expiration_pin']));
                $tentative = new Tentative(nombre:$row['d_nb_tentative']);
                $tentative->setDateDisponibiliteStr($row['d_date_debloquage']) ; 

                $compte = new Compte($row['id'], $utilisateur); 
                $compte->setPin($pin);
                $compte->setTentative($tentative);
                return $compte ;

            }
            return null;

        }

        public static function getByIdUtilisateur(Connection $connection, string $idUtilisateur): ?Compte
        {
            $query = 'SELECT * FROM Compte WHERE id_Utilisateur = ?';
            $stmt = $connection->executeQuery($query, [$idUtilisateur]);
            $row = $stmt->fetchAssociative();

            if ($row) {
                $utilisateur = Utilisateur::getById($connection, $row['id_utilisateur']);
                return new Compte($row['id'], $utilisateur);

            }
            return null;
        }

        public function insert(Connection $connection): void {

            if($this->getUtilisateur()==null) {

                throw new ModelException("Données du compte est invalide", $this);
            }
            $query = "INSERT INTO Compte (d_date_debloquage, d_date_expiration_pin,id_utilisateur) VALUES (?, ?, ?) RETURNING id";
            $stmt = $connection->executeQuery($query, [
                null,
                null,
                $this->getUtilisateur()->getId()
            ]);
            $id = $stmt->fetchOne();
            $this->setId($id);

            // d_nb_tentative, d_date_debloquage, d_pin_actuel, d_date_expiration_pin, id_utilisateur
        }
    }
?>