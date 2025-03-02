# Orleans-Go
# Plateforme de Ride-sharing OrleansGO

Application de ride-sharing permettant aux passagers de réserver des courses et aux chauffeurs de les accepter, avec suivi en temps réel.

## Architecture

Cette application est construite avec une architecture microservices utilisant :
- Spring Boot & Spring Cloud pour les services
- PostgreSQL pour le stockage des données persistantes
- Redis pour le cache et les sessions
- JWT pour l'authentification
- Spring Security pour la sécurité
- Swagger/OpenAPI pour la documentation des API

## Services

### Service Utilisateur (user-service)
- Gestion des comptes utilisateurs (passagers et chauffeurs)
- Authentication et autorisation
- Vérification des emails et numéros de téléphone
- Gestion des profils utilisateurs
- Support 2FA
- Système de réputation et d'évaluation

### Service Chauffeur (driver-service)
- Gestion des profils chauffeurs
- Validation des documents (permis, assurance, etc.)
- Gestion de la disponibilité
- Suivi de la position en temps réel
- Gestion des véhicules
- Calcul des gains et statistiques

### Service de Courses (ride-service)
- Création et gestion des courses
- Matching passager-chauffeur
- Calcul d'itinéraire et estimation du temps/prix
- Suivi en temps réel des courses
- Historique des courses
- Gestion des annulations

### Service de Paiement (payment-service)
- Intégration des moyens de paiement
- Gestion des transactions
- Calcul des commissions
- Gestion des remboursements
- Système de portefeuille électronique

### Service de Notification (notification-service)
- Notifications push
- SMS
- Emails
- Alertes en temps réel

### Service Support (support-service)
- Gestion des tickets support
- Chat en direct
- FAQ et base de connaissances
- Gestion des litiges

### Service Administratif (admin-service)
- Interface d'administration
- Tableaux de bord
- Gestion des commissions
- Rapports et analytics
- Configuration système

## Lancement du projet

Pour démarrer le service utilisateur :
```bash
mvn spring-boot:run -f service-utilisateur/pom.xml
```

## API Documentation

Une fois le service lancé, la documentation Swagger est accessible à :
- http://localhost:8081/swagger-ui/index.html
