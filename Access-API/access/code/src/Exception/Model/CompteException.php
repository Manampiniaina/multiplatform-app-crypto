<?php 

    namespace App\Exception\Model;

    use Exception;
    use App\Model\Compte;

    class CompteException extends Exception
    {
        // private Compte $compte;

        // public function setCompte(Compte $compte): void{
        //     $this->compte = $compte;
        // }

        // public function getCompte(): object{
        //     return $this->compte;
        // }

        public function __construct(string $message="Le compte est temporairement bloqué .")
        {
            $this->message = $message ;
            parent::__construct($this->message, 0, null);
        }
    } 

?>