<?php

namespace App\Controller;

use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class DefaultController
{
    #[Route('/', name: 'homepage')]
    public function index(): Response
    {
        return new Response('<h1>Bienvenue sur Symfony !</h1>');
    }
}
