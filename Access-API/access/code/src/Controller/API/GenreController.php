<?php
    
    // src/Controller/GenreController.php
    namespace App\Controller\API;

    use App\Model\Genre;
    use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
    use Symfony\Component\HttpFoundation\JsonResponse;
    use Symfony\Component\HttpFoundation\Response;
    use Symfony\Component\Routing\Annotation\Route;

    use Doctrine\DBAL\Connection;

    class GenreController extends AbstractController
    {
        private $genre;
        private Connection $connection ;


        // Injection du repository dans le constructeur
        public function __construct(Genre $genre, Connection $connection)
        {
            $this->genre = $genre;
            $this->connection = $connection;
        }

        #[Route('/genres', methods : 'GET')]

        public function getAllGenres(): JsonResponse
        {
            $genres = $this->genre->getAll($this->connection);
            return new JsonResponse( $genres, JsonResponse::HTTP_OK, ['Content-Type' => 'application/json']);

            }

        #[Route('/genre/{idGenre}', methods : 'GET')]

        public function getGenreById(string $idGenre): JsonResponse
        {
            $genre = $this->genre->getById($this->connection,$idGenre);

            if (!$genre) {
                return new JsonResponse(['error' => 'Genre not found'], Response::HTTP_NOT_FOUND);
            }

            return new JsonResponse( $genre);
        }
    }

?>
