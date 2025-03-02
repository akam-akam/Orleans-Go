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
- Tableaux de bord et rapports
- Gestion des commissions
- Validation des documents chauffeurs
- Surveillance des courses
- Configuration des paramètres système

## Lancement du projet

### Option 1: Docker Compose (recommandé pour le développement)

Pour démarrer tous les services avec Docker Compose :

```bash
docker-compose up
```

### Option 2: Démarrage individuel des services

Pour démarrer un service individuel :

```bash
# Service Utilisateur
mvn spring-boot:run -f service-utilisateur/pom.xml

# Service Conducteur
mvn spring-boot:run -f service-conducteur/pom.xml

# Service Administrateur
mvn spring-boot:run -f service-administrateur/pom.xml

# Service Trajet
mvn spring-boot:run -f service-trajet/pom.xml

# Service Notification
mvn spring-boot:run -f service-notification/pom.xml

# Service Paiement
mvn spring-boot:run -f service-paiement/pom.xml

# Service Support
mvn spring-boot:run -f service-support/pom.xml
```

## Tests

Pour exécuter les tests unitaires et d'intégration :

```bash
mvn test
```

Pour exécuter les tests de couverture :

```bash
mvn verify
```

## Documentation API

Une fois les services lancés, les documentations Swagger sont accessibles aux adresses suivantes :

- Service Utilisateur: http://localhost:8081/swagger-ui/index.html
- Service Conducteur: http://localhost:8082/swagger-ui/index.html
- Service Trajet: http://localhost:8083/swagger-ui/index.html
- Service Paiement: http://localhost:8084/swagger-ui/index.html
- Service Notification: http://localhost:8085/swagger-ui/index.html
- Service Support: http://localhost:8086/swagger-ui/index.html
- Service Administrateur: http://localhost:8087/swagger-ui/index.html

## Monitoring

Les services exposent des endpoints actuator pour le monitoring :

- http://localhost:808x/actuator

Ces endpoints peuvent être intégrés avec Prometheus et Grafana pour la visualisation.

## Base de données

Chaque service dispose de sa propre base de données dans l'instance PostgreSQL.
Les scripts de migration sont gérés par Flyway.

## Contribuer au projet

1. Forker le projet
2. Créer une branche (`git checkout -b feature/nom-fonctionnalite`)
3. Commiter les changements (`git commit -am 'Ajout d'une fonctionnalité'`)
4. Pousser la branche (`git push origin feature/nom-fonctionnalite`)
5. Créer une Pull Request
