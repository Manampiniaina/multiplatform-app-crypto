<?php 

    namespace App\Exception\Valeur;

    use Exception;
    use App\Model\Pin;

    class PinExpireException extends Exception
    {
        private Pin $pin ;

        public function setPin(Pin $pin): void{
            $this->pin = $pin;
        }

        public function getPin(): Pin{
            return $this->pin;
        }

        public function __construct(Pin $pin,string $message="Le pin actuel est déjà expiré")
        {
            $this->message = $message ;
            $this->setPin($pin);
            parent::__construct($this->message, 0, null);
        }
    } 

?>