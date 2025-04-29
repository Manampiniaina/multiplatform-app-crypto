<?php 

namespace App\Config;

class AppConfig 
{
    const LIMITE_NB_TENTATIVE_CONNEXION = 3;
    const DUREE_BLOQUAGE_COMPTE = '00:01:00';
    const DUREE_VALABILITE_PIN = '00:02:00';
    const LONGUEUR_PIN = 4;

    const DUREE_VALIDITE_TOKEN = '24:00:00';

    const timezone = 'Indian/Antananarivo';
}