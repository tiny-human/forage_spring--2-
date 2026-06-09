DROP DATABASE IF EXISTS forage;
CREATE DATABASE forage;
USE forage;

CREATE TABLE region (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL
);

CREATE TABLE district (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    id_region INT NOT NULL,
    CONSTRAINT fk_district_region FOREIGN KEY (id_region) REFERENCES region(id)
);

CREATE TABLE commune (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    id_district INT NOT NULL,
    CONSTRAINT fk_commune_district FOREIGN KEY (id_district) REFERENCES district(id)
);

CREATE TABLE client (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(120) NOT NULL,
    telephone VARCHAR(30),
    adresse VARCHAR(180)
);

CREATE TABLE statut (
    id INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(100) NOT NULL,
    code VARCHAR(50)
);

CREATE TABLE type_devis(
    id INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(100) NOT NULL
);

CREATE TABLE demande (
    id INT PRIMARY KEY AUTO_INCREMENT,
    reference VARCHAR(20) NOT NULL,
    id_client INT NOT NULL,
    lieu VARCHAR(150) NOT NULL,
    date_demande DATETIME NOT NULL,
    id_commune INT NOT NULL,
    CONSTRAINT fk_demande_client FOREIGN KEY (id_client) REFERENCES client(id),
    CONSTRAINT fk_demande_commune FOREIGN KEY (id_commune) REFERENCES commune(id)
);

    CREATE TABLE demande_statut (
        id INT PRIMARY KEY AUTO_INCREMENT,
        id_dm INT NOT NULL,
        id_statut INT NOT NULL,
        date_statut DATETIME NOT NULL,
        dt BIGINT NULL,
        observations VARCHAR(255),
        CONSTRAINT fk_demande_statut_demande FOREIGN KEY (id_dm) REFERENCES demande(id),
        CONSTRAINT fk_demande_statut_statut FOREIGN KEY (id_statut) REFERENCES statut(id)
    );

CREATE TABLE devis (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_demande INT NOT NULL,
    date_devis DATETIME NOT NULL,
    id_type INT NOT NULL,
    CONSTRAINT fk_devis_demande FOREIGN KEY (id_demande) REFERENCES demande(id),
     CONSTRAINT fk_devis_type   FOREIGN KEY (id_type)    REFERENCES type_devis(id)
);

CREATE TABLE devis_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_devis INT NOT NULL,
    libelle VARCHAR(150),
    quantite DOUBLE,
    pu DOUBLE,
    CONSTRAINT fk_ligne_devis_devis FOREIGN KEY (id_devis) REFERENCES devis(id)
);

CREATE TABLE parametre(
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_statut1 INT NOT NULL,
    id_statut2 INT NOT NULL,
    debut BIGINT NOT NULL,
    fin BIGINT NOT NULL,
    couleur VARCHAR(20),
    CONSTRAINT fk_param_statut1 FOREIGN KEY (id_statut1) REFERENCES statut(id),
    CONSTRAINT fk_param_statut2 FOREIGN KEY (id_statut2) REFERENCES statut(id)
);

INSERT INTO region (id, nom) VALUES
(1, 'Analamanga'),
(2, 'Vakinankaratra'),
(3, 'Atsinanana');

INSERT INTO district (id, nom, id_region) VALUES
(1, 'Antananarivo Renivohitra',  1),
(2, 'Antananarivo Atsimondrano', 1),
(3, 'Antsirabe I',               2),
(4, 'Antsirabe II',              2),
(5, 'Toamasina I',               3);

INSERT INTO commune (id, nom, id_district) VALUES
(1, 'Antananarivo',     1),
(2, 'Ambohidratrimo',   1),
(3, 'Andoharanofotsy',  2),
(4, 'Itaosy',           2),
(5, 'Antsirabe',        3),
(6, 'Manandona',        4),
(7, 'Toamasina',        5);

INSERT INTO client (id, nom, telephone, adresse) VALUES
(1, 'Rakoto',           '0341200001', 'Lot II M 22 Antananarivo');


-- Statuts de demande
INSERT INTO statut (libelle, code) VALUES
('Dossier creee',       'DEM_CREE'),
('Devis etude cree',    'DEV_ETU_CREE'),
('Devis etude accepte',  'DEV_ETU_ACCEPTE'),
('Devis forage cree',   'DEV_FOR_CREE'),
('Devis forage accepte', 'DEV_FOR_ACCEPTE'),
('Forage commence',     'FOR_DEBUT'),
('Forage termine',      'FOR_FIN');

INSERT INTO parametre (id_statut1, id_statut2, debut, fin, couleur) VALUES
(1, 2, 480, 600, 'jaune'),
(1, 2, 1440, 2880, 'rouge'),
(2, 3, 240, 300, 'jaune'),
(2, 3, 360, 480, 'rouge'),
(3, 4, 60, 120, 'jaune'),
(3, 4, 180, 240, 'rouge'),
(4, 5, 240, 480, 'jaune'),
(4, 5, 600, 720, 'rouge'),
(5, 6, 1200, 1800, 'jaune'),
(5, 6, 1801, 3600, 'rouge'),
(6, 7, 3600, 4200, 'jaune'),
(6, 7, 4800, 6000, 'rouge');

INSERT INTO type_devis (id, libelle) VALUES
(1, 'etude'),
(2, 'forage');

-- 1. Désactiver les contraintes de clés étrangères pour permettre la vidange
SET FOREIGN_KEY_CHECKS = 0;

-- 2. Vider les tables (TRUNCATE remet aussi les compteurs AUTO_INCREMENT à zéro)
TRUNCATE TABLE devis_detail;
TRUNCATE TABLE devis;
TRUNCATE TABLE demande_statut;
TRUNCATE TABLE demande;
TRUNCATE TABLE parametre;
TRUNCATE TABLE type_devis;
TRUNCATE TABLE statut;
TRUNCATE TABLE client;
TRUNCATE TABLE commune;
TRUNCATE TABLE district;
TRUNCATE TABLE region;

-- 3. Réactiver les contraintes de clés étrangères
SET FOREIGN_KEY_CHECKS = 1;
