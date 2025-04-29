<?php 

    namespace App\Exception\Model;

    use Exception;

    class ModelException extends Exception
    {
        private object $model ;

        public function setModel(object $model): void{
            $this->model = $model;
        }

        public function getModel(): object{
            return $this->model;
        }

        public function __construct(string $message="Mail inexistant", object $model)
        {
            $this->message = $message ;
            $this->setModel($model);
            parent::__construct($this->message, 0, null);
        }
    } 

?>