<?php 

    namespace App\Exception\Valeur;

    use Exception;
    use App\Model\TokenCompte;

    class TokenInvalideException extends Exception
    {
        private TokenCompte $token ;

        public function setToken(TokenCompte $token): void{
            $this->token = $token;
        }

        public function getToken(): TokenCompte{
            return $this->token;
        }

        public function __construct(TokenCompte $token, string $message="Correspondance du token invalide")
        {
            $this->message = $message ;
            $this->setToken($token);
            parent::__construct($this->message, 0, null);
        }
    } 

?>