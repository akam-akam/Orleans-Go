# Documentation API - OrleansGO

## Introduction

Ce document décrit les différentes API REST exposées par les microservices OrleansGO.

## Table des matières

1. [Service Utilisateur](#service-utilisateur)
2. [Service Conducteur](#service-conducteur)
3. [Service Trajet](#service-trajet)
4. [Service Paiement](#service-paiement)
5. [Service Notification](#service-notification)
6. [Service Administrateur](#service-administrateur)
7. [Service Support](#service-support)
8. [Service WebSocket](#service-websocket)
9. [Service Vérification](#service-verification)
10. [Service Parrainage](#service-parrainage)


## Service Utilisateur

Base URL: `http://localhost:8081/api`

### Endpoints

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

## Service Conducteur

Base URL: `http://localhost:8082/api`

### Endpoints

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/conducteurs` | Récupération de tous les conducteurs |
| GET | `/conducteurs/{id}` | Récupération d'un conducteur par son ID |
| POST | `/conducteurs` | Création d'un nouveau conducteur |
| PUT | `/conducteurs/{id}` | Mise à jour d'un conducteur |
| DELETE | `/conducteurs/{id}` | Suppression d'un conducteur |
| POST | `/conducteurs/{id}/documents` | Envoi de documents pour vérification |
| GET | `/conducteurs/{id}/documents` | Récupération des documents d'un conducteur |
| PUT | `/conducteurs/{id}/documents/{documentId}` | Mise à jour du statut d'un document |
| GET | `/conducteurs/{id}/vehicules` | Récupération des véhicules d'un conducteur |
| POST | `/conducteurs/{id}/vehicules` | Ajout d'un véhicule pour un conducteur |
| PUT | `/conducteurs/{id}/vehicules/{vehiculeId}` | Mise à jour d'un véhicule |
| DELETE | `/conducteurs/{id}/vehicules/{vehiculeId}` | Suppression d'un véhicule |

## Service Trajet

Base URL: `http://localhost:8083/api`

### Endpoints

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/trajets` | Récupération de tous les trajets |
| GET | `/trajets/{id}` | Récupération d'un trajet par son ID |
| POST | `/trajets` | Création d'un nouveau trajet |
| PUT | `/trajets/{id}` | Mise à jour d'un trajet |
| PUT | `/trajets/{id}/status` | Mise à jour du statut d'un trajet |
| GET | `/trajets/user/{userId}` | Récupération des trajets d'un utilisateur |
| GET | `/trajets/conducteur/{conducteurId}` | Récupération des trajets d'un conducteur |
| POST | `/trajets/{id}/position` | Mise à jour de la position en temps réel |
| GET | `/trajets/{id}/position` | Récupération de la position en temps réel |
| POST | `/trajets/estimate` | Estimation du prix et du temps de trajet |

## Service Paiement

Base URL: `http://localhost:8084/api`

### Endpoints

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/paiements` | Récupération de tous les paiements |
| GET | `/paiements/{id}` | Récupération d'un paiement par son ID |
| GET | `/paiements/utilisateur/{userId}` | Récupération des paiements d'un utilisateur |
| GET | `/paiements/trajet/{trajetId}` | Récupération des paiements pour un trajet |
| POST | `/paiements` | Création d'un nouveau paiement |
| PUT | `/paiements/{id}/confirmer` | Confirmation d'un paiement |
| PUT | `/paiements/{id}/annuler` | Annulation d'un paiement |
| GET | `/transactions` | Récupération de toutes les transactions |
| GET | `/transactions/{id}` | Récupération d'une transaction par son ID |
| GET | `/transactions/utilisateur/{userId}` | Récupération des transactions d'un utilisateur |
| POST | `/transactions/retrait` | Demande de retrait de fonds |

## Service Notification

Base URL: `http://localhost:8085/api`

### Endpoints

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

## Service Administrateur

Base URL: `http://localhost:8087/api`

### Endpoints Administration

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/administrateurs` | Récupération de tous les administrateurs |
| GET | `/administrateurs/{id}` | Récupération d'un administrateur par son ID |
| POST | `/administrateurs` | Création d'un administrateur |
| PUT | `/administrateurs/{id}` | Mise à jour d'un administrateur |
| DELETE | `/administrateurs/{id}` | Suppression d'un administrateur |

## Service Sécurité

Base URL: `http://localhost:8089/api`

### Endpoints Authentification

| Méthode | URL | Description |
| --- | --- | --- |
| POST | `/auth/login` | Authentification d'un utilisateur |
| POST | `/auth/refresh` | Rafraîchissement d'un token d'accès |
| POST | `/auth/logout` | Déconnexion d'un utilisateur |

## Service Véhicule

Base URL: `http://localhost:8090/api`

### Endpoints Véhicule

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/vehicules` | Récupération de tous les véhicules |
| GET | `/vehicules/{id}` | Récupération d'un véhicule par son ID |
| GET | `/vehicules/chauffeur/{chauffeurId}` | Récupération des véhicules d'un chauffeur |
| GET | `/vehicules/type/{typeVehicule}` | Récupération des véhicules par type |
| GET | `/vehicules/statut/{statut}` | Récupération des véhicules par statut |
| GET | `/vehicules/filtrer` | Filtrage des véhicules par type et statut |
| POST | `/vehicules` | Création d'un véhicule |
| PUT | `/vehicules/{id}` | Mise à jour d'un véhicule |
| DELETE | `/vehicules/{id}` | Suppression d'un véhicule |
| PATCH | `/vehicules/{id}/statut` | Changement du statut d'un véhicule |

### Endpoints Commission

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/commissions` | Récupération de toutes les commissions |
| GET | `/commissions/{id}` | Récupération d'une commission par son ID |
| POST | `/commissions` | Création d'une commission |
| PUT | `/commissions/{id}` | Mise à jour d'une commission |
| DELETE | `/commissions/{id}` | Suppression d'une commission |

### Endpoints Vérification Documents

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/verifications` | Récupération de toutes les vérifications |
| GET | `/verifications/{id}` | Récupération d'une vérification par son ID |
| POST | `/verifications` | Création d'une vérification |
| PUT | `/verifications/{id}` | Mise à jour d'une vérification |
| PUT | `/verifications/{id}/valider` | Valider les documents d'un conducteur |
| PUT | `/verifications/{id}/rejeter` | Rejeter les documents d'un conducteur |
| GET | `/verifications/en-attente` | Récupération des vérifications en attente |

### Endpoints Programme Parrainage

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/parrainages` | Récupération de tous les programmes de parrainage |
| GET | `/parrainages/{id}` | Récupération d'un programme de parrainage par son ID |
| POST | `/parrainages` | Création d'un programme de parrainage |
| PUT | `/parrainages/{id}` | Mise à jour d'un programme de parrainage |
| DELETE | `/parrainages/{id}` | Suppression d'un programme de parrainage |
| GET | `/parrainages/actifs` | Récupération des programmes de parrainage actifs |

### Endpoints Rapports et Statistiques

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/rapports` | Récupération de tous les rapports |
| GET | `/rapports/{id}` | Récupération d'un rapport par son ID |
| POST | `/rapports` | Création d'un rapport |
| PUT | `/rapports/{id}` | Mise à jour d'un rapport |
| DELETE | `/rapports/{id}` | Suppression d'un rapport |
| GET | `/rapports/periode` | Récupération des rapports par période |
| GET | `/rapports/type/{typeDonnees}` | Récupération des rapports par type de données |
| GET | `/rapports/administrateur/{adminId}` | Récupération des rapports créés par un administrateur |

### Endpoints Statistiques

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/statistiques` | Récupération de toutes les statistiques |
| GET | `/statistiques/{id}` | Récupération d'une statistique par son ID |
| GET | `/statistiques/type/{type}` | Récupération des statistiques par type |
| GET | `/statistiques/periode` | Récupération des statistiques par période |
| POST | `/statistiques` | Création d'une nouvelle statistique |
| PUT | `/statistiques/{id}` | Mise à jour d'une statistique |
| DELETE | `/statistiques/{id}` | Suppression d'une statistique |
| GET | `/statistiques/courses` | Récupération des statistiques sur les courses |
| GET | `/statistiques/chauffeurs` | Récupération des statistiques sur les chauffeurs |
| GET | `/statistiques/utilisateurs` | Récupération des statistiques sur les utilisateurs |
| POST | `/statistiques/generer-rapport` | Génération d'un rapport statistique complet |

### Endpoints Audit

| Méthode | URL | Description |
| --- | --- | --- |
| POST | `/audit` | Création d'un nouvel enregistrement d'audit |
| GET | `/audit/user/{username}` | Récupération des logs d'audit par utilisateur |
| GET | `/audit/entity` | Récupération des logs d'audit par entité |
| GET | `/audit/action/{actionType}` | Récupération des logs d'audit par type d'action |
| GET | `/audit/date-range` | Récupération des logs d'audit par plage de dates |
| GET | `/audit/search/user` | Recherche paginée des logs d'audit par nom d'utilisateur |
| GET | `/audit/search/entity` | Recherche paginée des logs d'audit par entité et ID |
| GET | `/audit/search/action-date` | Recherche paginée des logs d'audit par action et plage de dates |

### Endpoints Configuration Système

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/configurations` | Récupération de toutes les configurations |
| GET | `/configurations/{cle}` | Récupération d'une configuration par sa clé |
| GET | `/configurations/type/{type}` | Récupération des configurations par type |
| GET | `/configurations/search` | Recherche de configurations par description |
| POST | `/configurations` | Création d'une nouvelle configuration |
| PUT | `/configurations/{cle}` | Mise à jour d'une configuration |
| DELETE | `/configurations/{cle}` | Suppression d'une configuration |

### Endpoints Sauvegarde et Restauration

| Méthode | URL | Description |
| --- | --- | --- |
| POST | `/sauvegardes/creer` | Création d'une sauvegarde complète des données |
| POST | `/sauvegardes/restaurer` | Restauration d'une sauvegarde (nécessite un fichier) |
| GET | `/sauvegardes` | Récupération de la liste des sauvegardes disponibles |
| GET | `/sauvegardes/{id}` | Téléchargement d'une sauvegarde spécifique |
| DELETE | `/sauvegardes/{id}` | Suppression d'une sauvegarde |


## Service Support

Base URL: `http://localhost:8088/api`

### Endpoints Tickets

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/tickets` | Récupération de tous les tickets support |
| GET | `/tickets/{id}` | Récupération d'un ticket par son ID |
| POST | `/tickets` | Création d'un nouveau ticket |
| PUT | `/tickets/{id}` | Mise à jour d'un ticket |
| PUT | `/tickets/{id}/status` | Mise à jour du statut d'un ticket |
| GET | `/tickets/user/{userId}` | Récupération des tickets d'un utilisateur |
| GET | `/tickets/statut/{statut}` | Récupération des tickets par statut |
| GET | `/tickets/assigned/{agentId}` | Récupération des tickets assignés à un agent |

### Endpoints Messages

| Méthode | URL | Description |
| --- | --- | --- |
| POST | `/tickets/{ticketId}/messages` | Ajout d'un message à un ticket |
| GET | `/tickets/{ticketId}/messages` | Récupération des messages d'un ticket |

### Endpoints FAQ

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/faq` | Récupération de toutes les questions fréquentes actives |
| GET | `/faq/{id}` | Récupération d'une question fréquente par son ID |
| POST | `/faq` | Création d'une nouvelle question fréquente |
| PUT | `/faq/{id}` | Mise à jour d'une question fréquente |
| DELETE | `/faq/{id}` | Suppression d'une question fréquente |
| GET | `/faq/categorie/{categorie}` | Récupération des FAQs par catégorie |
| GET | `/faq/admin` | Récupération de toutes les FAQs (actives et inactives) |

## Service Evenement

Base URL: `http://localhost:8091/api`

### Endpoints Evenements

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/evenements` | Récupération de tous les événements |
| GET | `/evenements/{id}` | Récupération d'un événement par son ID |
| POST | `/evenements` | Création d'un nouvel événement |
| PUT | `/evenements/{id}` | Mise à jour d'un événement |
| DELETE | `/evenements/{id}` | Suppression d'un événement |
| GET | `/evenements/statut/{statut}` | Récupération des événements par statut |
| GET | `/evenements/publics` | Récupération des événements publics |
| GET | `/evenements/periode` | Récupération des événements par période |
| GET | `/evenements/createur/{createurId}` | Récupération des événements créés par un utilisateur |
| GET | `/evenements/categorie/{categorie}` | Récupération des événements par catégorie |
| GET | `/evenements/search` | Recherche d'événements par titre |
| PATCH | `/evenements/{id}/statut` | Mise à jour du statut d'un événement |

### Endpoints Inscriptions

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/inscriptions` | Récupération de toutes les inscriptions |
| GET | `/inscriptions/{id}` | Récupération d'une inscription par son ID |
| POST | `/inscriptions` | Création d'une inscription à un événement |
| PATCH | `/inscriptions/{id}/statut` | Mise à jour du statut d'une inscription |
| PATCH | `/inscriptions/{id}/confirmer-paiement` | Confirmation du paiement d'une inscription |
| DELETE | `/inscriptions/{id}` | Suppression d'une inscription |
| GET | `/inscriptions/evenement/{evenementId}` | Récupération des inscriptions pour un événement |
| GET | `/inscriptions/utilisateur/{utilisateurId}` | Récupération des inscriptions d'un utilisateur |
| GET | `/inscriptions/statut/{statut}` | Récupération des inscriptions par statut |
| GET | `/inscriptions/utilisateur/{utilisateurId}/statut/{statut}` | Récupération des inscriptions d'un utilisateur par statut |
| GET | `/inscriptions/evenement/{evenementId}/statut/{statut}` | Récupération des inscriptions pour un événement par statut |

## Service WebSocket

Base URL: `ws://localhost:8083/ws`

### Topics

| Topic | Description |
| --- | --- |
| `/topic/trajets/{trajetId}` | Suivi en temps réel d'un trajet |
| `/topic/chauffeurs/{chauffeurId}/position` | Position en temps réel d'un chauffeur |
| `/topic/utilisateurs/{userId}/notifications` | Notifications en temps réel pour un utilisateur |

### Publication (envoi)

| Destination | Description |
| --- | --- |
| `/app/position` | Mise à jour de la position d'un chauffeur |
| `/app/trajet/statut` | Mise à jour du statut d'un trajet |

## Service Vérification

Base URL: `http://localhost:8086/api`

### Endpoints

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/verifications` | Récupération de toutes les vérifications de documents |
| GET | `/verifications/{id}` | Récupération d'une vérification par son ID |
| GET | `/verifications/chauffeur/{chauffeurId}` | Récupération des vérifications d'un chauffeur |
| GET | `/verifications/en-attente` | Récupération des vérifications en attente |
| POST | `/verifications` | Création d'une nouvelle demande de vérification |
| PUT | `/verifications/{id}` | Mise à jour d'une vérification |

## Service Parrainage

Base URL: `http://localhost:8086/api`

### Endpoints

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/parrainages/programmes` | Récupération de tous les programmes de parrainage |
| GET | `/parrainages/programmes/{id}` | Récupération d'un programme par son ID |
| GET | `/parrainages/programmes/actif` | Récupération du programme actif |
| POST | `/parrainages/programmes` | Création d'un nouveau programme |
| PUT | `/parrainages/programmes/{id}` | Mise à jour d'un programme |
| PUT | `/parrainages/programmes/{id}/cloturer` | Clôture d'un programme |
| GET | `/parrainages/utilisateur/{userId}` | Récupération des parrainages d'un utilisateur |
| POST | `/parrainages/inviter` | Inviter un ami par email |
| POST | `/parrainages/valider` | Valider un code de parrainage |