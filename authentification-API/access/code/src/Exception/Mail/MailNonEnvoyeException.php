<?php

    namespace App\Exception\Mail;

    use Exception;
    use Throwable;

    class MailNonEnvoyeException extends Exception
    {
        public function __construct(string $message = "L'email n'a pas pu être envoyé.", ?Throwable $previous = null)
        {
            parent::__construct($message, 0, $previous);
        }
    } 

?>
