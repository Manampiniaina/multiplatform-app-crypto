<?php 

    namespace App\Exception\Valeur;

    use Exception;
    use App\Model\Pin;

    class PinInvalideException extends Exception
    {
        private Pin $pin ;

        public function setPin(Pin $pin): void{
            $this->pin = $pin;
        }

        public function getPin(): Pin{
            return $this->pin;
        }

        public function __construct(Pin $pin,string $message="Correspondance du pin invalide")
        {
            $this->message = $message ;
            $this->setPin($pin);
            parent::__construct($this->message, 0, null);
        }
    } 

?>