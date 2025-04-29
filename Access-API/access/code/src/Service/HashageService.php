<?php

namespace App\Service;

class HashageService
{
    // Champs variables
    private int $iteration;
    private string $pepper;
    private int $saltSize;
    private array $characters;

    public function __construct()
    {
        $this->iteration = 10000;
        $this->pepper = 'Kiady123';
        $this->saltSize = 16;
        $this->characters = array_merge(
            range(1, 9),
            range('a', 'z'),
            range('A', 'Z'),
            str_split('!@#$%^&*()_+-=[]{}|;:,.<>?/')
        );
    }

    private function getRandom(): string
    {
        $index = rand(0, count($this->characters) - 1);
        return $this->characters[$index];
    }

    // Génère un sel aléatoire
    public function getRandomSalt(): string
    {
        $salt = '';
        for ($i = 0; $i < $this->saltSize; $i++) {
            $salt .= $this->getRandom();
        }
        return $salt;
    }

    // Combine mot de passe, pepper et salt
    private function getRealPassword(string $password, string $salt): string
    {
        return $password . $this->pepper . $salt;
    }

    // Applique des itérations SHA256
    private function iterationSha(string $realPassword): string
    {
        for ($i = 0; $i < $this->iteration; $i++) {
            $realPassword = hash('sha256', $realPassword);
        }
        return $realPassword;
    }

    // Hashage avec Bcrypt
    private function bcryptage(string $passwordSha): string
    {
        return password_hash($passwordSha, PASSWORD_BCRYPT);
    }

    // Retourne un mot de passe hashé
    public function getHashPassword(string $password, string $salt): string
    {
        $realPassword = $this->getRealPassword($password, $salt);
        $sha = $this->iterationSha($realPassword);
        return $this->bcryptage($sha);
    }

    // Vérifie si le mot de passe correspond au hash
    public function verifyPassword(string $inputPassword, string $salt, string $hash): bool
    {
        $realPassword = $this->getRealPassword($inputPassword, $salt);
        $sha = $this->iterationSha($realPassword);
        return password_verify($sha, $hash);
    }
}
