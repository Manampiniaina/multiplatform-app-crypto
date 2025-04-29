<?php 


    namespace App\Service;

    use Symfony\Component\HttpFoundation\JsonResponse;

    class ReponseJSON {

        public function __construct()
        {}

        public function render(string $status, int $code, ?string $error, ?array $data) : JsonResponse{

            return new JsonResponse( [
                    'status' => $status,
                    'code' => $code,
                    'data' => $data,
                    'error' => $error
                ]

            );
        }
    
    }
?>
