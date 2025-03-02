
# Documentation OrleansGO

## Vue d'ensemble

OrleansGO est une application de covoiturage basée sur une architecture microservices. Ce document fournit une documentation complète sur l'ensemble du système, notamment l'architecture, les services, les API et les exigences techniques.

## Architecture

L'application OrleansGO est construite sur une architecture de microservices avec les composants suivants :

1. **Discovery Server** : Service de découverte Eureka pour l'enregistrement des services
2. **Config Server** : Service de configuration centralisée
3. **API Gateway** : Passerelle API pour router les requêtes
4. **Services métier** : Plusieurs services indépendants pour gérer différentes fonctionnalités

### Diagramme d'architecture

```
                             ┌────────────────┐
                             │   API Gateway  │
                             └────────────────┘
                                     │
                      ┌──────────────┼──────────────┐
        ┌─────────────┤              │              ├─────────────┐
┌───────▼───────┐ ┌───▼───┐    ┌─────▼─────┐   ┌────▼────┐  ┌─────▼─────┐
│ Discovery     │ │ Config│    │ Service   │   │ Service │  │ Service   │
│ Server        │ │ Server│    │ Utilisateur│  │ Conducteur│ │ Trajet   │
└───────────────┘ └───────┘    └───────────┘   └─────────┘  └───────────┘
                                     │               │            │
                      ┌──────────────┼──────────────┐            │
       ┌──────────────┤              │              ├────────────┘
┌──────▼───────┐ ┌────▼───────┐ ┌────▼───────┐ ┌────▼───────┐ 
│ Service      │ │ Service    │ │ Service    │ │ Service    │
│ Paiement     │ │ Notification│ │ Sécurité  │ │ Support    │
└──────────────┘ └────────────┘ └────────────┘ └────────────┘
        │              │              │             │
┌───────▼──────┐ ┌─────▼────┐ ┌───────▼────┐ ┌─────▼─────┐
│ Service      │ │ Service  │ │ Service    │ │ Service   │
│ Commission   │ │ Événement│ │ Véhicule   │ │ Admin     │
└──────────────┘ └──────────┘ └────────────┘ └───────────┘
```

## Services

### 1. Service Utilisateur

**Description :** Gère les informations des utilisateurs et l'authentification.

**Fonctionnalités principales :**
- Inscription et connexion des utilisateurs
- Gestion des profils utilisateurs
- Évaluations et commentaires
- Préférences utilisateurs

**API :** [Service Utilisateur API](#service-utilisateur-api)

### 2. Service Conducteur

**Description :** Gère les informations relatives aux conducteurs.

**Fonctionnalités principales :**
- Inscription et vérification des conducteurs
- Gestion des documents conducteurs
- Suivis des disponibilités et positions
- Gestion des véhicules

**API :** [Service Conducteur API](#service-conducteur-api)

### 3. Service Trajet

**Description :** Gère les trajets et les réservations.

**Fonctionnalités principales :**
- Création et gestion des trajets
- Recherche de trajets
- Réservation de trajets
- Suivi en temps réel
- Calcul d'itinéraire

**API :** [Service Trajet API](#service-trajet-api)

### 4. Service Paiement

**Description :** Gère tous les aspects liés aux paiements.

**Fonctionnalités principales :**
- Traitement des paiements
- Gestion des remboursements
- Portefeuille électronique
- Historique des transactions

**API :** [Service Paiement API](#service-paiement-api)

### 5. Service Notification

**Description :** Gère l'envoi de notifications aux utilisateurs.

**Fonctionnalités principales :**
- Notifications par email
- Notifications par SMS
- Notifications push
- Gestion des préférences de notification

**API :** [Service Notification API](#service-notification-api)

### 6. Service Administrateur

**Description :** Interface d'administration pour le personnel OrleansGO.

**Fonctionnalités principales :**
- Tableaux de bord administratifs
- Gestion des utilisateurs et conducteurs
- Validation des documents
- Suivi des activités
- Configuration du système

**API :** [Service Administrateur API](#service-administrateur-api)

### 7. Service Support

**Description :** Gère les demandes d'assistance des utilisateurs.

**Fonctionnalités principales :**
- Système de tickets
- FAQ
- Chat en direct
- Résolution des litiges

**API :** [Service Support API](#service-support-api)

### 8. Service Sécurité

**Description :** Gère l'authentification et les autorisations.

**Fonctionnalités principales :**
- Authentification via JWT
- Gestion des rôles et permissions
- Protection contre les attaques
- Audit de sécurité

**API :** [Service Sécurité API](#service-sécurité-api)

### 9. Service Commission

**Description :** Gère les commissions prélevées sur les trajets.

**Fonctionnalités principales :**
- Calcul des commissions
- Historique des commissions
- Rapports de commission
- Configuration des taux

**API :** [Service Commission API](#service-commission-api)

### 10. Service Événement

**Description :** Gère les événements système.

**Fonctionnalités principales :**
- Journalisation des événements système
- Historique des actions utilisateurs
- Génération de rapports
- Alertes système

**API :** [Service Événement API](#service-événement-api)

### 11. Service Véhicule

**Description :** Gère les informations des véhicules.

**Fonctionnalités principales :**
- Gestion des véhicules
- Vérification des documents véhicule
- Suivi de maintenance
- Classification des véhicules

**API :** [Service Véhicule API](#service-véhicule-api)

## API

### Service Utilisateur API

Base URL: `http://localhost:8081/api`

| Méthode | URL | Description |
| --- | --- | --- |
| POST | `/auth/register` | Inscription d'un nouvel utilisateur |
| POST | `/auth/login` | Connexion d'un utilisateur |
| POST | `/auth/verify` | Vérification de l'authentification à deux facteurs |
| GET | `/users` | Récupération de tous les utilisateurs |
| GET | `/users/{id}` | Récupération d'un utilisateur par son ID |
| PUT | `/users/{id}` | Mise à jour d'un utilisateur |
| DELETE | `/users/{id}` | Suppression d'un utilisateur |
| POST | `/users/{id}/ratings` | Ajout d'une note pour un utilisateur |
| GET | `/users/{id}/ratings` | Récupération des notes d'un utilisateur |

### Service Conducteur API

Base URL: `http://localhost:8082/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/conducteurs` | Récupération de tous les conducteurs |
| GET | `/conducteurs/{id}` | Récupération d'un conducteur par son ID |
| GET | `/conducteurs/utilisateur/{utilisateurId}` | Récupération d'un conducteur par son ID utilisateur |
| POST | `/conducteurs` | Création d'un nouveau conducteur |
| PUT | `/conducteurs/{id}` | Mise à jour d'un conducteur |
| PATCH | `/conducteurs/{id}/statut` | Mise à jour du statut d'un conducteur |
| PATCH | `/conducteurs/{id}/position` | Mise à jour de la position d'un conducteur |
| PATCH | `/conducteurs/{id}/disponibilite` | Mise à jour de la disponibilité d'un conducteur |
| GET | `/conducteurs/disponibles` | Récupération des conducteurs disponibles |
| GET | `/conducteurs/proximite` | Recherche des conducteurs à proximité |
| GET | `/conducteurs/{id}/documents` | Récupération des documents d'un conducteur |
| POST | `/conducteurs/{id}/documents` | Ajout d'un document pour un conducteur |
| PATCH | `/documents/{id}/validation` | Validation ou rejet d'un document |
| GET | `/conducteurs/{id}/vehicules` | Récupération des véhicules d'un conducteur |
| POST | `/conducteurs/{id}/vehicules` | Ajout d'un véhicule pour un conducteur |
| PATCH | `/conducteurs/{id}/vehicules/{vehiculeId}/principal` | Définir un véhicule comme principal |
| PATCH | `/vehicules/{id}/statut` | Changement du statut d'un véhicule |

### Service Trajet API

Base URL: `http://localhost:8083/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/trajets` | Récupération de tous les trajets |
| GET | `/trajets/{id}` | Récupération d'un trajet par son ID |
| POST | `/trajets` | Création d'un nouveau trajet |
| PUT | `/trajets/{id}` | Mise à jour d'un trajet |
| DELETE | `/trajets/{id}` | Suppression d'un trajet |
| GET | `/trajets/utilisateur/{utilisateurId}` | Récupération des trajets d'un utilisateur |
| GET | `/trajets/conducteur/{conducteurId}` | Récupération des trajets d'un conducteur |
| PATCH | `/trajets/{id}/statut` | Mise à jour du statut d'un trajet |
| POST | `/trajets/{id}/reservations` | Ajout d'une réservation |
| GET | `/trajets/{id}/reservations` | Récupération des réservations d'un trajet |
| PATCH | `/reservations/{id}/statut` | Mise à jour du statut d'une réservation |
| POST | `/trajets/recherche` | Recherche de trajets |
| GET | `/trajets/{id}/positions` | Récupération de l'historique des positions d'un trajet |
| POST | `/trajets/{id}/positions` | Ajout d'une position pour un trajet |

### Service Paiement API

Base URL: `http://localhost:8084/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/paiements` | Récupération de tous les paiements |
| GET | `/paiements/{id}` | Récupération d'un paiement par son ID |
| POST | `/paiements` | Création d'un nouveau paiement |
| GET | `/paiements/utilisateur/{utilisateurId}` | Récupération des paiements d'un utilisateur |
| GET | `/paiements/conducteur/{conducteurId}` | Récupération des paiements d'un conducteur |
| GET | `/paiements/trajet/{trajetId}` | Récupération des paiements d'un trajet |
| POST | `/paiements/{id}/remboursement` | Remboursement d'un paiement |
| GET | `/paiements/{id}/transactions` | Récupération des transactions d'un paiement |
| POST | `/utilisateurs/{id}/portefeuille/recharge` | Recharge du portefeuille d'un utilisateur |
| GET | `/utilisateurs/{id}/portefeuille` | Récupération du solde du portefeuille d'un utilisateur |
| GET | `/utilisateurs/{id}/portefeuille/transactions` | Récupération des transactions du portefeuille d'un utilisateur |

### Service Notification API

Base URL: `http://localhost:8085/api`

| Méthode | URL | Description |
| --- | --- | --- |
| POST | `/notifications/email` | Envoi d'un email |
| POST | `/notifications/sms` | Envoi d'un SMS |
| POST | `/notifications/push` | Envoi d'une notification push |
| GET | `/notifications/user/{userId}` | Récupération des notifications d'un utilisateur |
| PUT | `/notifications/{id}/read` | Marquer une notification comme lue |
| DELETE | `/notifications/{id}` | Suppression d'une notification |
| POST | `/notifications/subscribe` | Abonnement aux notifications |
| DELETE | `/notifications/unsubscribe` | Désabonnement des notifications |

### Service Administrateur API

Base URL: `http://localhost:8086/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/administrateurs` | Récupération de tous les administrateurs |
| GET | `/administrateurs/{id}` | Récupération d'un administrateur par son ID |
| POST | `/administrateurs` | Création d'un administrateur |
| PUT | `/administrateurs/{id}` | Mise à jour d'un administrateur |
| DELETE | `/administrateurs/{id}` | Suppression d'un administrateur |
| GET | `/dashboard/statistiques` | Récupération des statistiques pour le tableau de bord |
| GET | `/documents/validation` | Récupération des documents en attente de validation |
| POST | `/documents/{id}/validation` | Validation ou rejet d'un document |
| GET | `/configurations` | Récupération des configurations du système |
| PUT | `/configurations/{id}` | Mise à jour d'une configuration |
| GET | `/rapports/trajets` | Génération d'un rapport sur les trajets |
| GET | `/rapports/paiements` | Génération d'un rapport sur les paiements |
| GET | `/rapports/utilisateurs` | Génération d'un rapport sur les utilisateurs |
| GET | `/rapports/commissions` | Génération d'un rapport sur les commissions |

### Service Support API

Base URL: `http://localhost:8087/api`

| Méthode | URL | Description |
| --- | --- | --- |
| POST | `/tickets` | Création d'un nouveau ticket |
| GET | `/tickets` | Récupération de tous les tickets |
| GET | `/tickets/{id}` | Récupération d'un ticket par son ID |
| PUT | `/tickets/{id}` | Mise à jour d'un ticket |
| GET | `/tickets/utilisateur/{utilisateurId}` | Récupération des tickets d'un utilisateur |
| PATCH | `/tickets/{id}/statut` | Mise à jour du statut d'un ticket |
| PATCH | `/tickets/{id}/agent` | Assignation d'un ticket à un agent |
| POST | `/tickets/{ticketId}/messages` | Ajout d'un message à un ticket |
| GET | `/tickets/{ticketId}/messages` | Récupération des messages d'un ticket |
| GET | `/faq` | Récupération de toutes les questions fréquentes |
| GET | `/faq/{id}` | Récupération d'une question fréquente par son ID |
| POST | `/faq` | Création d'une nouvelle question fréquente |
| PUT | `/faq/{id}` | Mise à jour d'une question fréquente |
| DELETE | `/faq/{id}` | Suppression d'une question fréquente |

### Service Sécurité API

Base URL: `http://localhost:8088/api`

| Méthode | URL | Description |
| --- | --- | --- |
| POST | `/auth/login` | Authentification d'un utilisateur |
| POST | `/auth/refresh` | Rafraîchissement d'un token d'accès |
| POST | `/auth/logout` | Déconnexion d'un utilisateur |
| GET | `/auth/permissions` | Récupération des permissions |
| GET | `/auth/roles` | Récupération des rôles |
| POST | `/auth/roles` | Création d'un nouveau rôle |
| PUT | `/auth/roles/{id}` | Mise à jour d'un rôle |
| DELETE | `/auth/roles/{id}` | Suppression d'un rôle |
| POST | `/auth/roles/{id}/permissions` | Attribution de permissions à un rôle |
| GET | `/auth/audit` | Récupération des logs d'audit |

### Service Commission API

Base URL: `http://localhost:8089/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/commissions` | Récupération de toutes les commissions |
| GET | `/commissions/{id}` | Récupération d'une commission par son ID |
| GET | `/commissions/conducteur/{conducteurId}` | Récupération des commissions d'un conducteur |
| GET | `/commissions/trajet/{trajetId}` | Récupération de la commission d'un trajet |
| GET | `/taux-commissions` | Récupération de tous les taux de commission |
| GET | `/taux-commissions/{id}` | Récupération d'un taux de commission par son ID |
| POST | `/taux-commissions` | Création d'un nouveau taux de commission |
| PUT | `/taux-commissions/{id}` | Mise à jour d'un taux de commission |
| DELETE | `/taux-commissions/{id}` | Suppression d'un taux de commission |
| GET | `/rapports/commissions/periode` | Génération d'un rapport de commissions par période |
| GET | `/rapports/commissions/conducteur/{conducteurId}` | Génération d'un rapport de commissions par conducteur |

### Service Événement API

Base URL: `http://localhost:8090/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/evenements` | Récupération de tous les événements |
| GET | `/evenements/{id}` | Récupération d'un événement par son ID |
| POST | `/evenements` | Création d'un nouvel événement |
| GET | `/evenements/type/{type}` | Récupération des événements par type |
| GET | `/evenements/utilisateur/{utilisateurId}` | Récupération des événements d'un utilisateur |
| GET | `/evenements/periode` | Récupération des événements par période |
| GET | `/rapports/evenements` | Génération d'un rapport d'événements |
| GET | `/alertes` | Récupération de toutes les alertes système |
| POST | `/alertes` | Création d'une nouvelle alerte |
| PUT | `/alertes/{id}` | Mise à jour d'une alerte |
| DELETE | `/alertes/{id}` | Suppression d'une alerte |

### Service Véhicule API

Base URL: `http://localhost:8091/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/vehicules` | Récupération de tous les véhicules |
| GET | `/vehicules/{id}` | Récupération d'un véhicule par son ID |
| POST | `/vehicules` | Création d'un nouveau véhicule |
| PUT | `/vehicules/{id}` | Mise à jour d'un véhicule |
| DELETE | `/vehicules/{id}` | Suppression d'un véhicule |
| GET | `/vehicules/conducteur/{conducteurId}` | Récupération des véhicules d'un conducteur |
| PATCH | `/vehicules/{id}/statut` | Mise à jour du statut d'un véhicule |
| GET | `/vehicules/{id}/documents` | Récupération des documents d'un véhicule |
| POST | `/vehicules/{id}/documents` | Ajout d'un document pour un véhicule |
| PATCH | `/vehicules/documents/{id}/validation` | Validation ou rejet d'un document de véhicule |
| GET | `/types-vehicules` | Récupération de tous les types de véhicules |
| GET | `/types-vehicules/{id}` | Récupération d'un type de véhicule par son ID |
| POST | `/types-vehicules` | Création d'un nouveau type de véhicule |
| PUT | `/types-vehicules/{id}` | Mise à jour d'un type de véhicule |
| DELETE | `/types-vehicules/{id}` | Suppression d'un type de véhicule |

## WebSocket

Base URL: `ws://localhost:8083/ws`

| Topic | Description |
| --- | --- |
| `/topic/trajets/{trajetId}` | Suivi en temps réel d'un trajet |
| `/topic/chauffeurs/{chauffeurId}/position` | Position en temps réel d'un chauffeur |
| `/topic/utilisateurs/{userId}/notifications` | Notifications en temps réel pour un utilisateur |

## Technologie

- **Languages**: Java 17
- **Framework**: Spring Boot 3.x
- **Microservices**: Spring Cloud
- **Base de données**: PostgreSQL
- **Cache**: Redis
- **Messaging**: Kafka
- **Authentification**: OAuth2 / JWT
- **Conteneurisation**: Docker
- **Orchestration**: Docker Compose

## Tests

Chaque service dispose de :
- **Tests unitaires**: Tests des composants individuels
- **Tests d'intégration**: Tests des interactions entre composants
- **Tests de bout en bout**: Tests simulant les scénarios utilisateurs réels

## Déploiement

L'application peut être déployée de deux manières :

### Option 1: Docker Compose (Développement)

```bash
docker-compose up
```

### Option 2: Démarrage individuel (Développement)

```bash
cd service-xxx
./mvnw spring-boot:run
```

## Surveillance et Journalisation

- **Actuator** - Endpoints pour la surveillance système
- **Prometheus** - Collecte de métriques
- **Grafana** - Visualisation des métriques
- **ELK Stack** - Gestion centralisée des logs

## Sécurité

- **Spring Security** - Sécurisation des endpoints
- **JWT** - Authentification sans état
- **OAuth2** - Autorisation

## Architecture de données

Chaque service possède sa propre base de données pour garantir un découplage complet.

## Développement

- **Git Flow** - Modèle de branchement pour la gestion des versions
- **CI/CD** - Intégration et déploiement continus
- **Tests automatisés** - Exécution des tests à chaque build
# Documentation OrleansGO

## Vue d'ensemble

OrleansGO est une application de covoiturage basée sur une architecture microservices. Ce document fournit une documentation complète sur l'ensemble du système, notamment l'architecture, les services, les API et les exigences techniques.

## Architecture

L'application OrleansGO est construite sur une architecture de microservices avec les composants suivants :

1. **Discovery Server** : Service de découverte Eureka pour l'enregistrement des services
2. **Config Server** : Service de configuration centralisée
3. **API Gateway** : Passerelle API pour router les requêtes
4. **Services métier** : Plusieurs services indépendants pour gérer différentes fonctionnalités

### Diagramme d'architecture

```
                             ┌────────────────┐
                             │   API Gateway  │
                             └────────────────┘
                                     │
                      ┌──────────────┼──────────────┐
        ┌─────────────┤              │              ├─────────────┐
┌───────▼───────┐ ┌───▼───┐    ┌─────▼─────┐   ┌────▼────┐  ┌─────▼─────┐
│ Discovery     │ │ Config│    │ Service   │   │ Service │  │ Service   │
│ Server        │ │ Server│    │ Utilisateur│  │ Conducteur│ │ Trajet   │
└───────────────┘ └───────┘    └───────────┘   └─────────┘  └───────────┘
                                     │               │            │
                      ┌──────────────┼──────────────┐            │
       ┌──────────────┤              │              ├────────────┘
┌──────▼───────┐ ┌────▼───────┐ ┌────▼───────┐ ┌────▼───────┐ 
│ Service      │ │ Service    │ │ Service    │ │ Service    │
│ Paiement     │ │ Notification│ │ Sécurité  │ │ Support    │
└──────────────┘ └────────────┘ └────────────┘ └────────────┘
        │              │              │             │
┌───────▼──────┐ ┌─────▼────┐ ┌───────▼────┐ ┌─────▼─────┐
│ Service      │ │ Service  │ │ Service    │ │ Service   │
│ Commission   │ │ Événement│ │ Véhicule   │ │ Admin     │
└──────────────┘ └──────────┘ └────────────┘ └───────────┘
```

## Services

### 1. Service Utilisateur

**Description :** Gère les informations des utilisateurs et l'authentification.

**Fonctionnalités principales :**
- Inscription et connexion des utilisateurs
- Gestion des profils utilisateurs
- Évaluations et commentaires
- Préférences utilisateurs

**API :** [Service Utilisateur API](#service-utilisateur-api)

### 2. Service Conducteur

**Description :** Gère les informations relatives aux conducteurs.

**Fonctionnalités principales :**
- Inscription et vérification des conducteurs
- Gestion des documents conducteurs
- Suivis des disponibilités et positions
- Gestion des véhicules

**API :** [Service Conducteur API](#service-conducteur-api)

### 3. Service Trajet

**Description :** Gère les trajets et les réservations.

**Fonctionnalités principales :**
- Création et gestion des trajets
- Recherche de trajets
- Réservation de trajets
- Suivi en temps réel
- Calcul d'itinéraire

**API :** [Service Trajet API](#service-trajet-api)

### 4. Service Paiement

**Description :** Gère tous les aspects liés aux paiements.

**Fonctionnalités principales :**
- Traitement des paiements
- Gestion des remboursements
- Portefeuille électronique
- Historique des transactions

**API :** [Service Paiement API](#service-paiement-api)

### 5. Service Notification

**Description :** Gère l'envoi de notifications aux utilisateurs.

**Fonctionnalités principales :**
- Notifications par email
- Notifications par SMS
- Notifications push
- Gestion des préférences de notification

**API :** [Service Notification API](#service-notification-api)

### 6. Service Administrateur

**Description :** Interface d'administration pour le personnel OrleansGO.

**Fonctionnalités principales :**
- Tableaux de bord administratifs
- Gestion des utilisateurs et conducteurs
- Validation des documents
- Suivi des activités
- Configuration du système

**API :** [Service Administrateur API](#service-administrateur-api)

### 7. Service Support

**Description :** Gère les demandes d'assistance des utilisateurs.

**Fonctionnalités principales :**
- Système de tickets
- FAQ
- Chat en direct
- Résolution des litiges

**API :** [Service Support API](#service-support-api)

### 8. Service Sécurité

**Description :** Gère l'authentification et les autorisations.

**Fonctionnalités principales :**
- Authentification via JWT
- Gestion des rôles et permissions
- Protection contre les attaques
- Audit de sécurité

**API :** [Service Sécurité API](#service-sécurité-api)

### 9. Service Commission

**Description :** Gère les commissions prélevées sur les trajets.

**Fonctionnalités principales :**
- Calcul des commissions
- Historique des commissions
- Rapports de commission
- Configuration des taux

**API :** [Service Commission API](#service-commission-api)

### 10. Service Événement

**Description :** Gère les événements système.

**Fonctionnalités principales :**
- Journalisation des événements système
- Historique des actions utilisateurs
- Génération de rapports
- Alertes système

**API :** [Service Événement API](#service-événement-api)

### 11. Service Véhicule

**Description :** Gère les informations des véhicules.

**Fonctionnalités principales :**
- Gestion des véhicules
- Vérification des documents véhicule
- Suivi de maintenance
- Classification des véhicules

**API :** [Service Véhicule API](#service-véhicule-api)

## API

### Service Utilisateur API

Base URL: `http://localhost:8081/api`

| Méthode | URL | Description |
| --- | --- | --- |
| POST | `/auth/register` | Inscription d'un nouvel utilisateur |
| POST | `/auth/login` | Connexion d'un utilisateur |
| POST | `/auth/verify` | Vérification de l'authentification à deux facteurs |
| GET | `/users` | Récupération de tous les utilisateurs |
| GET | `/users/{id}` | Récupération d'un utilisateur par son ID |
| PUT | `/users/{id}` | Mise à jour d'un utilisateur |
| DELETE | `/users/{id}` | Suppression d'un utilisateur |
| POST | `/users/{id}/ratings` | Ajout d'une note pour un utilisateur |
| GET | `/users/{id}/ratings` | Récupération des notes d'un utilisateur |

### Service Conducteur API

Base URL: `http://localhost:8082/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/conducteurs` | Récupération de tous les conducteurs |
| GET | `/conducteurs/{id}` | Récupération d'un conducteur par son ID |
| GET | `/conducteurs/utilisateur/{utilisateurId}` | Récupération d'un conducteur par son ID utilisateur |
| POST | `/conducteurs` | Création d'un nouveau conducteur |
| PUT | `/conducteurs/{id}` | Mise à jour d'un conducteur |
| PATCH | `/conducteurs/{id}/statut` | Mise à jour du statut d'un conducteur |
| PATCH | `/conducteurs/{id}/position` | Mise à jour de la position d'un conducteur |
| PATCH | `/conducteurs/{id}/disponibilite` | Mise à jour de la disponibilité d'un conducteur |
| GET | `/conducteurs/disponibles` | Récupération des conducteurs disponibles |
| GET | `/conducteurs/proximite` | Recherche des conducteurs à proximité |
| GET | `/conducteurs/{id}/documents` | Récupération des documents d'un conducteur |
| POST | `/conducteurs/{id}/documents` | Ajout d'un document pour un conducteur |
| PATCH | `/documents/{id}/validation` | Validation ou rejet d'un document |
| GET | `/conducteurs/{id}/vehicules` | Récupération des véhicules d'un conducteur |
| POST | `/conducteurs/{id}/vehicules` | Ajout d'un véhicule pour un conducteur |
| PATCH | `/conducteurs/{id}/vehicules/{vehiculeId}/principal` | Définir un véhicule comme principal |
| PATCH | `/vehicules/{id}/statut` | Changement du statut d'un véhicule |

### Service Trajet API

Base URL: `http://localhost:8083/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/trajets` | Récupération de tous les trajets |
| GET | `/trajets/{id}` | Récupération d'un trajet par son ID |
| POST | `/trajets` | Création d'un nouveau trajet |
| PUT | `/trajets/{id}` | Mise à jour d'un trajet |
| DELETE | `/trajets/{id}` | Suppression d'un trajet |
| GET | `/trajets/utilisateur/{utilisateurId}` | Récupération des trajets d'un utilisateur |
| GET | `/trajets/conducteur/{conducteurId}` | Récupération des trajets d'un conducteur |
| PATCH | `/trajets/{id}/statut` | Mise à jour du statut d'un trajet |
| POST | `/trajets/{id}/reservations` | Ajout d'une réservation |
| GET | `/trajets/{id}/reservations` | Récupération des réservations d'un trajet |
| PATCH | `/reservations/{id}/statut` | Mise à jour du statut d'une réservation |
| POST | `/trajets/recherche` | Recherche de trajets |
| GET | `/trajets/{id}/positions` | Récupération de l'historique des positions d'un trajet |
| POST | `/trajets/{id}/positions` | Ajout d'une position pour un trajet |

### Service Paiement API

Base URL: `http://localhost:8084/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/paiements` | Récupération de tous les paiements |
| GET | `/paiements/{id}` | Récupération d'un paiement par son ID |
| POST | `/paiements` | Création d'un nouveau paiement |
| GET | `/paiements/utilisateur/{utilisateurId}` | Récupération des paiements d'un utilisateur |
| GET | `/paiements/conducteur/{conducteurId}` | Récupération des paiements d'un conducteur |
| GET | `/paiements/trajet/{trajetId}` | Récupération des paiements d'un trajet |
| POST | `/paiements/{id}/remboursement` | Remboursement d'un paiement |
| GET | `/paiements/{id}/transactions` | Récupération des transactions d'un paiement |
| POST | `/utilisateurs/{id}/portefeuille/recharge` | Recharge du portefeuille d'un utilisateur |
| GET | `/utilisateurs/{id}/portefeuille` | Récupération du solde du portefeuille d'un utilisateur |
| GET | `/utilisateurs/{id}/portefeuille/transactions` | Récupération des transactions du portefeuille d'un utilisateur |

### Service Notification API

Base URL: `http://localhost:8085/api`

| Méthode | URL | Description |
| --- | --- | --- |
| POST | `/notifications/email` | Envoi d'un email |
| POST | `/notifications/sms` | Envoi d'un SMS |
| POST | `/notifications/push` | Envoi d'une notification push |
| GET | `/notifications/user/{userId}` | Récupération des notifications d'un utilisateur |
| PUT | `/notifications/{id}/read` | Marquer une notification comme lue |
| DELETE | `/notifications/{id}` | Suppression d'une notification |
| POST | `/notifications/subscribe` | Abonnement aux notifications |
| DELETE | `/notifications/unsubscribe` | Désabonnement des notifications |

### Service Administrateur API

Base URL: `http://localhost:8086/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/administrateurs` | Récupération de tous les administrateurs |
| GET | `/administrateurs/{id}` | Récupération d'un administrateur par son ID |
| POST | `/administrateurs` | Création d'un administrateur |
| PUT | `/administrateurs/{id}` | Mise à jour d'un administrateur |
| DELETE | `/administrateurs/{id}` | Suppression d'un administrateur |
| GET | `/dashboard/statistiques` | Récupération des statistiques pour le tableau de bord |
| GET | `/documents/validation` | Récupération des documents en attente de validation |
| POST | `/documents/{id}/validation` | Validation ou rejet d'un document |
| GET | `/configurations` | Récupération des configurations du système |
| PUT | `/configurations/{id}` | Mise à jour d'une configuration |
| GET | `/rapports/trajets` | Génération d'un rapport sur les trajets |
| GET | `/rapports/paiements` | Génération d'un rapport sur les paiements |
| GET | `/rapports/utilisateurs` | Génération d'un rapport sur les utilisateurs |
| GET | `/rapports/commissions` | Génération d'un rapport sur les commissions |

### Service Support API

Base URL: `http://localhost:8087/api`

| Méthode | URL | Description |
| --- | --- | --- |
| POST | `/tickets` | Création d'un nouveau ticket |
| GET | `/tickets` | Récupération de tous les tickets |
| GET | `/tickets/{id}` | Récupération d'un ticket par son ID |
| PUT | `/tickets/{id}` | Mise à jour d'un ticket |
| GET | `/tickets/utilisateur/{utilisateurId}` | Récupération des tickets d'un utilisateur |
| PATCH | `/tickets/{id}/statut` | Mise à jour du statut d'un ticket |
| PATCH | `/tickets/{id}/agent` | Assignation d'un ticket à un agent |
| POST | `/tickets/{ticketId}/messages` | Ajout d'un message à un ticket |
| GET | `/tickets/{ticketId}/messages` | Récupération des messages d'un ticket |
| GET | `/faq` | Récupération de toutes les questions fréquentes |
| GET | `/faq/{id}` | Récupération d'une question fréquente par son ID |
| POST | `/faq` | Création d'une nouvelle question fréquente |
| PUT | `/faq/{id}` | Mise à jour d'une question fréquente |
| DELETE | `/faq/{id}` | Suppression d'une question fréquente |

### Service Sécurité API

Base URL: `http://localhost:8088/api`

| Méthode | URL | Description |
| --- | --- | --- |
| POST | `/auth/login` | Authentification d'un utilisateur |
| POST | `/auth/refresh` | Rafraîchissement d'un token d'accès |
| POST | `/auth/logout` | Déconnexion d'un utilisateur |
| GET | `/auth/permissions` | Récupération des permissions |
| GET | `/auth/roles` | Récupération des rôles |
| POST | `/auth/roles` | Création d'un nouveau rôle |
| PUT | `/auth/roles/{id}` | Mise à jour d'un rôle |
| DELETE | `/auth/roles/{id}` | Suppression d'un rôle |
| POST | `/auth/roles/{id}/permissions` | Attribution de permissions à un rôle |
| GET | `/auth/audit` | Récupération des logs d'audit |

### Service Commission API

Base URL: `http://localhost:8089/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/commissions` | Récupération de toutes les commissions |
| GET | `/commissions/{id}` | Récupération d'une commission par son ID |
| GET | `/commissions/conducteur/{conducteurId}` | Récupération des commissions d'un conducteur |
| GET | `/commissions/trajet/{trajetId}` | Récupération de la commission d'un trajet |
| GET | `/taux-commissions` | Récupération de tous les taux de commission |
| GET | `/taux-commissions/{id}` | Récupération d'un taux de commission par son ID |
| POST | `/taux-commissions` | Création d'un nouveau taux de commission |
| PUT | `/taux-commissions/{id}` | Mise à jour d'un taux de commission |
| DELETE | `/taux-commissions/{id}` | Suppression d'un taux de commission |
| GET | `/rapports/commissions/periode` | Génération d'un rapport de commissions par période |
| GET | `/rapports/commissions/conducteur/{conducteurId}` | Génération d'un rapport de commissions par conducteur |

### Service Événement API

Base URL: `http://localhost:8090/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/evenements` | Récupération de tous les événements |
| GET | `/evenements/{id}` | Récupération d'un événement par son ID |
| POST | `/evenements` | Création d'un nouvel événement |
| GET | `/evenements/type/{type}` | Récupération des événements par type |
| GET | `/evenements/utilisateur/{utilisateurId}` | Récupération des événements d'un utilisateur |
| GET | `/evenements/periode` | Récupération des événements par période |
| GET | `/rapports/evenements` | Génération d'un rapport d'événements |
| GET | `/alertes` | Récupération de toutes les alertes système |
| POST | `/alertes` | Création d'une nouvelle alerte |
| PUT | `/alertes/{id}` | Mise à jour d'une alerte |
| DELETE | `/alertes/{id}` | Suppression d'une alerte |

### Service Véhicule API

Base URL: `http://localhost:8091/api`

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/vehicules` | Récupération de tous les véhicules |
| GET | `/vehicules/{id}` | Récupération d'un véhicule par son ID |
| POST | `/vehicules` | Création d'un nouveau véhicule |
| PUT | `/vehicules/{id}` | Mise à jour d'un véhicule |
| DELETE | `/vehicules/{id}` | Suppression d'un véhicule |
| GET | `/vehicules/conducteur/{conducteurId}` | Récupération des véhicules d'un conducteur |
| PATCH | `/vehicules/{id}/statut` | Mise à jour du statut d'un véhicule |
| GET | `/vehicules/{id}/documents` | Récupération des documents d'un véhicule |
| POST | `/vehicules/{id}/documents` | Ajout d'un document pour un véhicule |
| PATCH | `/vehicules/documents/{id}/validation` | Validation ou rejet d'un document de véhicule |
| GET | `/types-vehicules` | Récupération de tous les types de véhicules |
| GET | `/types-vehicules/{id}` | Récupération d'un type de véhicule par son ID |
| POST | `/types-vehicules` | Création d'un nouveau type de véhicule |
| PUT | `/types-vehicules/{id}` | Mise à jour d'un type de véhicule |
| DELETE | `/types-vehicules/{id}` | Suppression d'un type de véhicule |

## WebSocket

Base URL: `ws://localhost:8083/ws`

| Topic | Description |
| --- | --- |
| `/topic/trajets/{trajetId}` | Suivi en temps réel d'un trajet |
| `/topic/chauffeurs/{chauffeurId}/position` | Position en temps réel d'un chauffeur |
| `/topic/utilisateurs/{userId}/notifications` | Notifications en temps réel pour un utilisateur |

## Technologie

- **Languages**: Java 17
- **Framework**: Spring Boot 3.x
- **Microservices**: Spring Cloud
- **Base de données**: PostgreSQL
- **Cache**: Redis
- **Messaging**: Kafka
- **Authentification**: OAuth2 / JWT
- **Conteneurisation**: Docker
- **Orchestration**: Docker Compose

## Tests

Chaque service dispose de :
- **Tests unitaires**: Tests des composants individuels
- **Tests d'intégration**: Tests des interactions entre composants
- **Tests de bout en bout**: Tests simulant les scénarios utilisateurs réels

## Déploiement

L'application peut être déployée de deux manières :

### Option 1: Docker Compose (Développement)

```bash
docker-compose up
```

### Option 2: Démarrage individuel (Développement)

```bash
cd service-xxx
./mvnw spring-boot:run
```

## Surveillance et Journalisation

- **Actuator** - Endpoints pour la surveillance système
- **Prometheus** - Collecte de métriques
- **Grafana** - Visualisation des métriques
- **ELK Stack** - Gestion centralisée des logs

## Sécurité

- **Spring Security** - Sécurisation des endpoints
- **JWT** - Authentification sans état
- **OAuth2** - Autorisation

## Architecture de données

Chaque service possède sa propre base de données pour garantir un découplage complet.

## Développement

- **Git Flow** - Modèle de branchement pour la gestion des versions
- **CI/CD** - Intégration et déploiement continus
- **Tests automatisés** - Exécution des tests à chaque build
