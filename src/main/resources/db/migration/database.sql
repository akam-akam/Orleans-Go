CREATE DATABASE IF NOT EXISTS `orleango_administration` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

-- Création des tables pour le service Administrateur
CREATE TABLE IF NOT EXISTS administrateur (
                                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    actif BOOLEAN DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS verification_documents (
                                                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    chauffeur_id UUID REFERENCES chauffeur(id),
    permis_valide BOOLEAN DEFAULT FALSE,
    carte_grise_valide BOOLEAN DEFAULT FALSE,
    assurance_valide BOOLEAN DEFAULT FALSE,
    date_verification TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS gestion_litiges (
                                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plaignant_id UUID REFERENCES utilisateur(id),
    mis_en_cause_id UUID REFERENCES utilisateur(id),
    description TEXT NOT NULL,
    statut VARCHAR(50) NOT NULL,
    date_ouverture TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_resolution TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS commission (
                                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    taux_commission DECIMAL(5,2) NOT NULL,
    type_vehicule VARCHAR(100),
    region VARCHAR(100),
    date_debut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_fin TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS rapport_admin (
                                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    genere_par UUID REFERENCES administrateur(id),
    type VARCHAR(50) NOT NULL,
    date_generation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    contenu BYTEA
    );
