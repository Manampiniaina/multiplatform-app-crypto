<?php 

    namespace App\Exception\Valeur;

    use Exception;

    class ValeurInvalideException extends Exception
    {
        private object $model ;

        public function setModel(object $model): void{
            $this->model = $model;
        }

        public function getModel(): object{
            return $this->model;
        }

        public function __construct(string $message="Valeur invalide", object $model)
        {
            $this->message = $message ;
            $this->setModel($model);
            parent::__construct($this->message, 0, null);
        }
    } 

?>