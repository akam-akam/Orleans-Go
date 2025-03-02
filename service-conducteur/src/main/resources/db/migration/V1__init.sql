
CREATE TABLE IF NOT EXISTS conducteurs (
    id BIGSERIAL PRIMARY KEY,
    utilisateur_id BIGINT NOT NULL,
    numero_permis VARCHAR(50),
    date_expiration_permis TIMESTAMP,
    documents_valides BOOLEAN DEFAULT FALSE,
    disponible BOOLEAN DEFAULT FALSE,
    statut VARCHAR(50) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    derniere_position TIMESTAMP,
    note_globale DOUBLE PRECISION DEFAULT 0,
    nombre_courses INTEGER DEFAULT 0,
    nombre_avis INTEGER DEFAULT 0,
    date_creation TIMESTAMP NOT NULL,
    date_modification TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS documents (
    id BIGSERIAL PRIMARY KEY,
    conducteur_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    nom_fichier VARCHAR(255) NOT NULL,
    url_fichier VARCHAR(500) NOT NULL,
    statut VARCHAR(50) NOT NULL,
    commentaire_validation TEXT,
    valide_par BIGINT,
    date_validation TIMESTAMP,
    date_expiration TIMESTAMP,
    date_creation TIMESTAMP NOT NULL,
    date_modification TIMESTAMP NOT NULL,
    FOREIGN KEY (conducteur_id) REFERENCES conducteurs(id)
);

CREATE TABLE IF NOT EXISTS vehicules_conducteurs (
    id BIGSERIAL PRIMARY KEY,
    conducteur_id BIGINT NOT NULL,
    vehicule_id BIGINT NOT NULL,
    vehicule_principal BOOLEAN DEFAULT FALSE,
    date_debut TIMESTAMP NOT NULL,
    date_fin TIMESTAMP,
    statut VARCHAR(50) NOT NULL,
    date_creation TIMESTAMP NOT NULL,
    date_modification TIMESTAMP NOT NULL,
    FOREIGN KEY (conducteur_id) REFERENCES conducteurs(id)
);

CREATE INDEX idx_conducteurs_utilisateur_id ON conducteurs(utilisateur_id);
CREATE INDEX idx_conducteurs_disponible ON conducteurs(disponible);
CREATE INDEX idx_conducteurs_statut ON conducteurs(statut);
CREATE INDEX idx_documents_conducteur_id ON documents(conducteur_id);
CREATE INDEX idx_documents_type ON documents(type);
CREATE INDEX idx_documents_statut ON documents(statut);
CREATE INDEX idx_vehicules_conducteurs_conducteur_id ON vehicules_conducteurs(conducteur_id);
CREATE INDEX idx_vehicules_conducteurs_vehicule_id ON vehicules_conducteurs(vehicule_id);
