<?php 

    namespace App\Exception\Model;

    use Exception;

    class AuthentificationException extends Exception
    {

        public function __construct(string $message="Mot de passe éroné")
        {
            $this->message = $message ;
            parent::__construct($this->message, 0, null);
        }
    } 

?>