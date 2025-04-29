<?php

    namespace App\Model;

    use Doctrine\DBAL\Connection;
    use App\Exception\Model\AuthentificationException;
    use App\Config\AppConfig;
    use App\Exception\Valeur\PinInvalideException;
    use App\Exception\Valeur\PinExpireException;
    use App\Service\ServiceMail;
    use App\Service\Utilitaire;
    use App\Model\Pin;
    use App\Model\TokenCompte;

    class ConnectionUtilisateur
    {

        public Compte $compte;

        public function getCompte() : Compte {
            return $this->compte;
        }

        public function setCompte(?Compte $compte){
            $this->compte = $compte;
        }


        public function __construct(?Compte $compte = new Compte())
        {
            $this->compte = $compte;
        }

        public function checkExpirationPin() {
            $dateActuelle = Utilitaire::getDateActuelle();
            // echo 'Date actuelle : ' . $dateActuelle->format('Y-m-d H:i:s T') . PHP_EOL;
            // echo 'Date déblocage : ' . $this->compte->getPin()->getDateExpiration()->format('Y-m-d H:i:s T') . PHP_EOL;
            if($dateActuelle > $this->compte->getPin()->getDateExpiration()) {
                throw new PinExpireException(pin:new Pin($this->compte->getPin()->getPin()));
            }
            // if($dateActuelle >$this->getCompte()->getTentative()->dateDisponibilite){
            //     throw new AuthentificationException('Votre compte est bloqué');

            // }
        }
    
        public function verifier_pin(string $pin) : bool {
            try {
                $this->checkExpirationPin();
            }
            catch(\Exception $e) {
                throw $e;
            }
            if($pin==$this->compte->getPin()->getPin()) {
                return true;
            }
            throw new PinInvalideException(pin:new Pin($pin));
        }

        public function mettre_a_jour_pin(Connection $connection) : void {
            $utilitaire = new Utilitaire();
            $nouveau_pin = $utilitaire->genere_pin();
            $this->compte->getPin()->setPin($nouveau_pin);
            $this->compte->getPin()->setDateExpiration($utilitaire->getDatePrevuee(AppConfig::DUREE_VALABILITE_PIN));
            $this->compte->getPin()->setIdCompte($this->compte->getId());
            $this->compte->getPin()->insert($connection);
            $this->compte->updatePIN($connection);
        }

        public function verifierNbTentative(Connection $connection) : void {
            if($this->compte->getTentative()->getNombre()>=AppConfig::LIMITE_NB_TENTATIVE_CONNEXION-1) {
                $this->compte->bloquer($connection);
            }
            else {
                $this->compte->ajouterTentativeEchouer($connection);
            }
        }

        public function processus_connection(Connection $connection, Utilisateur $utilisateur, ServiceMail $serviceMail) : void {
            try {
                $compte = $utilisateur->se_connecter($connection);
                $this->setCompte($compte);
                $this->mettre_a_jour_pin($connection);
                $context = array(
                    'pin' => $this->compte->getPin()->getPin()
                );
                $serviceMail->envoyerMail($this->compte->getUtilisateur()->getMail(), $context, 'emails/pinverification.html.twig',"Vérification d\'identité");
            }
            catch(AuthentificationException $e) {
                $compte = new Compte(utilisateur:$utilisateur);
                $compte->setCompteByMail($connection);
                $this->setCompte($compte);
                $this->verifierNbTentative($connection);
                throw $e;  
            }
            catch(\Exception $e) {
                throw $e;
            }
        }

        public function creerToken(): TokenCompte
        {
            $utilitaire = new Utilitaire();
            $token = new TokenCompte();
            $token->setId(uniqid());
            $token->setValeur(bin2hex(random_bytes(16)));
            $token->setDateExpiration($utilitaire->getDatePrevuee(AppConfig::DUREE_VALIDITE_TOKEN));
            $token->setIdCompte($this->compte->getId());

            return $token;
        }

        public function reinitialiserNbTentative(Connection $connection): void
        {
            $sql = 'UPDATE compte SET d_nb_tentative = 0 WHERE id = :id';
            $stmt = $connection->prepare($sql);
            $stmt->bindValue('id', $this->compte->getId());
            $stmt->executeStatement();
        }

        public function processus_check_pin(Connection $connection, string $pin): TokenCompte
        {
            try {
                $this->getCompte()->estCompteBloque($connection) ;
                $this->verifier_pin($pin);
                $token = $this->creerToken();    
                $token->insert($connection);
                $this->reinitialiserNbTentative($connection);

                return $token;
            // } catch (PinInvalideException | PinExpireException $e) {
            } catch (PinInvalideException $e) {
                $this->verifierNbTentative($connection);
                throw $e;
            } catch (\Exception $e) {
                throw new \RuntimeException("Erreur lors de la vérification du PIN : " . $e->getMessage());
            } 
        }
    }
?>