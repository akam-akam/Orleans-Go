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

Base URL: `http://localhost:8086/api`

### Endpoints

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/administrateurs` | Récupération de tous les administrateurs |
| GET | `/administrateurs/{id}` | Récupération d'un administrateur par son ID |
| POST | `/administrateurs` | Création d'un nouvel administrateur |
| PUT | `/administrateurs/{id}` | Mise à jour d'un administrateur |
| DELETE | `/administrateurs/{id}` | Suppression d'un administrateur |
| GET | `/commissions` | Récupération de toutes les commissions |
| GET | `/commissions/{id}` | Récupération d'une commission par son ID |
| GET | `/commissions/current` | Récupération de la commission actuelle |
| POST | `/commissions` | Création d'une nouvelle commission |
| PUT | `/commissions/{id}` | Mise à jour d'une commission |
| PUT | `/commissions/{id}/cloturer` | Clôture d'une commission |
| POST | `/commissions/ajuster` | Ajustement du taux de commission |
| GET | `/parrainages` | Récupération de tous les programmes de parrainage |
| GET | `/parrainages/{id}` | Récupération d'un programme de parrainage par son ID |
| GET | `/parrainages/current` | Récupération du programme de parrainage actuel |
| POST | `/parrainages` | Création d'un nouveau programme de parrainage |
| PUT | `/parrainages/{id}` | Mise à jour d'un programme de parrainage |
| PUT | `/parrainages/{id}/cloturer` | Clôture d'un programme de parrainage |
| POST | `/parrainages/configurer` | Configuration d'un nouveau programme de parrainage |

## Service Support

Base URL: `http://localhost:8087/api`

### Endpoints

| Méthode | URL | Description |
| --- | --- | --- |
| GET | `/tickets` | Récupération de tous les tickets support |
| GET | `/tickets/{id}` | Récupération d'un ticket par son ID |
| POST | `/tickets` | Création d'un nouveau ticket |
| PUT | `/tickets/{id}` | Mise à jour d'un ticket |
| PUT | `/tickets/{id}/status` | Mise à jour du statut d'un ticket |
| POST | `/tickets/{id}/messages` | Ajout d'un message à un ticket |
| GET | `/tickets/{id}/messages` | Récupération des messages d'un ticket |
| GET | `/faq` | Récupération de toutes les questions fréquentes |
| GET | `/faq/{id}` | Récupération d'une question fréquente par son ID |
| POST | `/faq` | Création d'une nouvelle question fréquente |
| PUT | `/faq/{id}` | Mise à jour d'une question fréquente |
| DELETE | `/faq/{id}` | Suppression d'une question fréquente |

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