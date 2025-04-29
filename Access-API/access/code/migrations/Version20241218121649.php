<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20241218121649 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('DROP SEQUENCE s_genre CASCADE');
        $this->addSql('DROP SEQUENCE s_utilisateur CASCADE');
        $this->addSql('DROP SEQUENCE s_compte CASCADE');
        $this->addSql('DROP SEQUENCE s_pin CASCADE');
        $this->addSql('DROP SEQUENCE s_tentative CASCADE');
        $this->addSql('DROP SEQUENCE s_token CASCADE');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE SEQUENCE s_genre INCREMENT BY 1 MINVALUE 1 START 1');
        $this->addSql('CREATE SEQUENCE s_utilisateur INCREMENT BY 1 MINVALUE 1 START 1');
        $this->addSql('CREATE SEQUENCE s_compte INCREMENT BY 1 MINVALUE 1 START 1');
        $this->addSql('CREATE SEQUENCE s_pin INCREMENT BY 1 MINVALUE 1 START 1');
        $this->addSql('CREATE SEQUENCE s_tentative INCREMENT BY 1 MINVALUE 1 START 1');
        $this->addSql('CREATE SEQUENCE s_token INCREMENT BY 1 MINVALUE 1 START 1');
    }
}
