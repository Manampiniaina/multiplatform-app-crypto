<?php

namespace App\Controller;

use App\Service\EmailService;
use App\Service\ServiceMail;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class EmailController extends AbstractController
{

    #[Route("/mail", methods: "GET")]
    // public function sendInvitation(ServiceMail $serviceMail): Response
    // {
    //         try {
    //             $serviceMail->envoyerMail("vetsojoella@gmail.com","");
    //         } catch(\Exception $e){
    //             return new JsonResponse(["erreur ".$e]); 
    //         }

    //     return new Response('Invitation envoyée avec succès.');
    // }

    #[Route("/mailVerification", methods: ["GET"])]
    public function verification(ServiceMail $serviceMail): Response
    {
        $adresse = "kiadymanohisoa@gmail.com";
        try {
            $exists = $serviceMail->estValide($adresse);
    
            return new JsonResponse([
                'success' => true,
                'email' => $adresse,
                'exists' => $exists,
            ], Response::HTTP_OK);
    
        } catch (\Exception $e) {
            // Log l'erreur pour le débogage (optionnel)
            // $this->logger->error('Erreur lors de la vérification de l\'email : ' . $e->getMessage());
    
            return new JsonResponse([
                'success' => false,
                'message' => 'Une erreur est survenue.',
                'details' => $e->getMessage(), // A retirer ou anonymiser en production
            ], Response::HTTP_INTERNAL_SERVER_ERROR);
        }
    }
    
}
