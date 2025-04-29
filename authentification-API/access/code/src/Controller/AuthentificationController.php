<?php

namespace App\Controller;

use App\Model\ConnectionUtilisateur;
use App\Service\ReponseJSON;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;
use Doctrine\DBAL\Connection;
use App\Model\Utilisateur;
use App\Model\Compte;
use App\Service\ServiceMail;
use App\Exception\Valeur\PinInvalideException;

class AuthentificationController extends AbstractController
{

    private Connection $connection;

    public function __construct(Connection $connection)
    {
        $this->connection = $connection;
    }

    #[Route("/api/auth/login", methods: ["POST"])]
    public function connectionUtilisateur (Request $request, ServiceMail $serviceMail): JsonResponse
    {
        $reponseJson = new ReponseJSON();
        $status = '';
        $code = null;
        $error = '';

        $data = json_decode($request->getContent(), true);
        $mailUtilisateur = $data['mail'];
        $motDePasse = $data['motdepasse'];
        $utilisateur = new Utilisateur(mail:$mailUtilisateur, mdp:$motDePasse);
        $connectionUtilisateur = new ConnectionUtilisateur();
        $datas = null;
        try {
            $connectionUtilisateur->processus_connection($this->connection, $utilisateur, $serviceMail);            
            $datas = array('message' => 'Vérifiez votre boîte mail car le code secret vous a été transféré', "id"=>$connectionUtilisateur->getCompte()->getId());
            // $datas = array('message' => 'Authentification réussie', "id"=>$connectionUtilisateur->getCompte()->getId());
            $status = 'success';
            $code = 200;
        }
        catch(\Exception $e) {
            $status = 'error';
            $code = 500;
            $error = $e->getMessage();
        }
        return $reponseJson->render($status, $code, $error,$datas);
    } 
    

    #[Route("/api/auth/check-pin", methods: ["POST"])]
    public function checkPin(Request $request, Connection $connection): JsonResponse
    {
        $reponseJson = new ReponseJSON();
        $data = json_decode($request->getContent(), true);
    
        $idCompte = $data['id_compte'] ?? null;
        $pin = $data['pin'] ?? null;
        $compte = null ;
    
        if (!$idCompte || !$pin) {
            return $reponseJson->render('error', 400, 'ID du compte ou PIN manquant.', null);
        }
    
        try {
            $compte = Compte::getById($connection, $idCompte);
            // echo('checkPin Valeur de la date d_tentative est '.$compte->getTentative()->getDateDisponibilite());
    
            if (!$compte) {
                return $reponseJson->render('error', 404, 'Compte introuvable.', null);
            }
    
            $connectionUtilisateur = new ConnectionUtilisateur($compte);
            $token = $connectionUtilisateur->processus_check_pin($connection, $pin);
    
            return $reponseJson->render('success', 200, null, [
                'message' => 'PIN vérifié avec succès.',
                'token' => $token->getValeur()
            ]);
    
        } catch (PinInvalideException $e) {
            return $reponseJson->render('error', 401, 'PIN incorrect.', $data);
        } catch (\Exception $e) {
            return $reponseJson->render('error', 500, $e->getMessage(),$data );
        }
    }


}
