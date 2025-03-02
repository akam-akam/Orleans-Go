# OrleansGO - Plateforme de Ride-sharing

## Vue d'ensemble
OrleansGO est une plateforme de mise en relation entre chauffeurs et passagers pour des services de transport. Elle propose également la location de véhicules pour les entreprises et particuliers. L'application est accessible via web (React.js) et mobile (Android & iOS).

## Architecture microservices

### Infrastructure
- **API Gateway** : Point d'entrée unique pour tous les clients
- **Discovery Server** : Service d'enregistrement et de découverte (Eureka)
- **Config Server** : Configuration centralisée
- **Auth Server** : Authentification et autorisation

### Services métier
#### Service Utilisateur (service-utilisateur)
- Gestion des profils utilisateurs
- Authentification et autorisation
- Système de réputation et d'évaluation
- Tests: Unitaires et d'intégration

#### Service Chauffeur (service-conducteur)
- Gestion des profils chauffeurs
- Validation des documents (permis, assurance, etc.)
- Gestion de la disponibilité
- Suivi de la position en temps réel
- Gestion des véhicules
- Calcul des gains et statistiques
- Tests: Unitaires et d'intégration

#### Service de Courses (service-trajet)
- Création et gestion des courses
- Matching passager-chauffeur
- Calcul d'itinéraire et estimation du temps/prix
- Suivi en temps réel des courses
- Historique des courses
- Gestion des annulations

#### Service de Commission (service-commission)
- Gestion des taux de commission
- Calcul des commissions sur les trajets
- Historique des commissions appliquées
- Statistiques des commissions par chauffeur
- Rapports sur les commissions par période Tests: Unitaires

#### Service Support (service-support)
- Gestion des tickets de support
- Système de messagerie pour l'assistance
- Base de connaissances (FAQ)
- Suivi des problèmes et résolution
- Tests: Unitaires et d'intégration

#### Service de Paiement (service-paiement)
- Intégration des moyens de paiement
- Gestion des transactions
- Calcul des commissions
- Gestion des remboursements
- Système de portefeuille électronique
- Tests: Unitaires et d'intégration

#### Service de Notification (service-notification)
- Notifications push
- SMS
- Emails
- Alertes en temps réel
- Tests: Unitaires et d'intégration

#### Service Support (service-support)
- Gestion des tickets support
- Chat en direct
- FAQ et base de connaissances
- Gestion des litiges
- Tests: Unitaires et d'intégration

#### Service Administratif (service-administrateur)
- Interface d'administration
- Tableaux de bord et rapports
- Gestion des commissions
- Validation des documents chauffeurs
- Surveillance des courses
- Configuration des paramètres système
- Tests: Unitaires et d'intégration

## Technologie
- Java 17
- Spring Boot 3.x
- Spring Cloud
- Spring Data JPA
- PostgreSQL
- Redis
- Docker
- Kafka
- OAuth2 / JWT

## Lancement du projet

### Option 1: Docker Compose (recommandé pour le développement)

Pour démarrer tous les services avec Docker Compose:

```bash
docker-compose up
```

### Option 2: Démarrage individuel des services

Pour démarrer un service individuel:

```bash
cd service-xxx
./mvnw spring-boot:run
```

## Tests
Chaque service possède ses propres tests unitaires et d'intégration.

Pour exécuter les tests d'un service:
```bash
cd service-xxx
./mvnw test
```

## Documentation API
Chaque service expose sa documentation via Swagger UI:
- Service Utilisateur: http://localhost:8081/swagger-ui.html
- Service Chauffeur: http://localhost:8082/swagger-ui.html
- Service Trajet: http://localhost:8083/swagger-ui.html
- Service Paiement: http://localhost:8084/swagger-ui.html
- Service Notification: http://localhost:8085/swagger-ui.html
- Service Administrateur: http://localhost:8086/swagger-ui.html
- Service Support: http://localhost:8087/swagger-ui.html