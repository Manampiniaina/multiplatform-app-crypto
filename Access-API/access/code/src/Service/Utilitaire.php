<?php 

namespace App\Service;

use App\Config\AppConfig;
use DateTime;
use DateInterval;

class Utilitaire 
{

    public function __construct() {

    }

    public function getDatePrevuee($intervalle) {

        $currentDateTime = Utilitaire::getDateActuelle();

        list($hours, $minutes, $seconds) = explode(':', $intervalle);

        $interval = new DateInterval('PT' . $hours . 'H' . $minutes . 'M' . $seconds . 'S');
        
        $prochaineDate = clone $currentDateTime;
        $prochaineDate->add($interval);

        return $prochaineDate;
    }
    
    public function genere_pin(): string {
        $longueurPin = AppConfig::LONGUEUR_PIN;
        $pin = '';
        for ($i = 0; $i < $longueurPin; $i++) {
            $pin .= rand(0, 9);  
        }
        return (string) $pin;
    }

    public static function getDateActuelle(): DateTime{
        // $timezone = new \DateTimeZone('Indian/Antananarivo');
        // $currentDateTime = new DateTime('now', $timezone);
        return new DateTime();
    }

    public static function setTimeZone(?DateTime $date): void{
        // if($date != null) {
        //     $timezone = new \DateTimeZone('Indian/Antananarivo');
        //     $date->setTimezone($timezone);
        // }
       
    }

    public static function changeTimeZoneDateTime(DateTime $dateTime) : DateTime {
        $timezoneobj = new \DateTimeZone(AppConfig::timezone);
        $dateTime->setTimezone($timezoneobj);
        return $dateTime;
    }
}