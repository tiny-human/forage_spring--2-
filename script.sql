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
    duree BIGINT NOT NULL,
    couleur VARCHAR(20),
    CONSTRAINT fk_param_statut1 FOREIGN KEY (id_statut1) REFERENCES statut(id),
    CONSTRAINT fk_param_statut2 FOREIGN KEY (id_statut2) REFERENCES statut(id)
);

INSERT INTO parametre (id_statut1, id_statut2, duree, couleur) VALUES
(1, 2, 60, 'rouge');  
(2, 3, 2880, 'bleue'),    
(3, 3, 4320, 'orange'),  
(4, 4, 4320, 'rouge');  

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
(1, 'Rakoto Jean',           '0341200001', 'Lot II M 22 Antananarivo'),
(2, 'Rasoanaivo Miora',      '0321200002', 'Itaosy Antananarivo'),
(3, 'Andrianina Tiana',      '0331200003', 'Antsirabe Centre'),
(4, 'Raharinirina Fara',     '0381200004', 'Toamasina Sud');

-- Statuts de demande
INSERT INTO statut (libelle, code) VALUES
('Demande creee',       'DEM_CREE'),
('Demande acceptee',    'DEM_ACCEPTEE'),
('Forage commence',     'FOR_DEBUT'),
('Suspendu',            'SUSPENDU'),
('Forage termine',      'FOR_FIN'),
('Demande rejetee',     'DEM_REJETEE'),
('Devis etude cree',    'DEV_ETU_CREE'),
('Devis etude refuse',  'DEV_ETU_REFUSE'),
('Devis etude annule',  'DEV_ETU_ANNULE'),
('Devis forage cree',   'DEV_FOR_CREE'),
('Devis forage refuse', 'DEV_FOR_REFUSE'),
('Devis forage annule', 'DEV_FOR_ANNULE');

INSERT INTO type_devis (id, libelle) VALUES
(1, 'etude'),
(2, 'forage');

INSERT INTO demande (id, reference, id_client, lieu, date_demande, id_commune) VALUES
-- CORRECTION : 6 colonnes, 6 valeurs (supprimé la 7e valeur parasite)
(1, 'DE001', 1, 'Terrain Avarabohitra',     '2026-05-01 09:15:00', 3),
(2, 'DE002', 2, 'Pres du marche communal',  '2026-05-02 14:30:00', 4),
(3, 'DE003', 3, 'Secteur agricole nord',    '2026-05-03 10:45:00', 5),
(4, 'DE004', 4, 'Zone portuaire',           '2026-05-04 08:20:00', 7);

INSERT INTO demande_statut (id, id_dm, id_statut, date_statut, dt) VALUES
-- DT est porté par la ligne du statut courant (temps écoulé depuis le statut précédent)
(1, 1, 1, '2026-05-01 09:15:00', NULL),
(2, 2, 1, '2026-05-02 14:30:00', NULL),
(3, 2, 2, '2026-05-03 08:00:00', 1050),
(4, 3, 1, '2026-05-03 10:45:00', NULL),
(5, 3, 2, '2026-05-04 09:00:00', 1335),
(6, 3, 3, '2026-05-05 11:10:00', 1570),
(7, 4, 1, '2026-05-04 08:20:00', NULL),
(8, 4, 4, '2026-05-06 16:30:00', 3370);

INSERT INTO devis (id, id_demande, date_devis, id_type) VALUES
(1, 1, '2026-05-02 11:00:00', 1),
(2, 3, '2026-05-05 13:40:00', 2);

INSERT INTO devis_detail (id, id_devis, libelle, quantite, pu) VALUES
(1, 1, 'Etude hydrogeologique', 1,  250000),
(2, 1, 'Forage 80m',            80, 18000),
(3, 2, 'Tubage PVC',            60, 22000),
(4, 2, 'Pompe immergee',        1,  650000);

-- 1. Désactiver les contraintes de clés étrangères pour permettre la vidange
SET FOREIGN_KEY_CHECKS = 0;

-- 2. Vider les tables (TRUNCATE remet aussi les compteurs AUTO_INCREMENT à zéro)
TRUNCATE TABLE devis_detail;
TRUNCATE TABLE devis;
TRUNCATE TABLE demande_statut;
TRUNCATE TABLE demande;
TRUNCATE TABLE type_devis;
TRUNCATE TABLE statut;
TRUNCATE TABLE client;
TRUNCATE TABLE commune;
TRUNCATE TABLE district;
TRUNCATE TABLE region;

-- 3. Réactiver les contraintes de clés étrangères
SET FOREIGN_KEY_CHECKS = 1;