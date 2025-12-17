# Mini-LMS : Système d'Apprentissage en Ligne Simplifié

## 📋 Description du Projet

Ce projet implémente un **système éducatif modulaire (LMS)** basé sur une architecture **microservices**. Il permet la gestion des cours, étudiants, inscriptions et notes de manière découplée et résiliente.

## 🏗️ Architecture

```
                    ┌─────────────────┐
                    │   API Gateway   │
                    │   (Port 8080)   │
                    └────────┬────────┘
                             │
              ┌──────────────┼──────────────┐
              │              │              │
     ┌────────▼────────┐     │     ┌────────▼────────┐
     │ Student Service │     │     │ Course Service  │
     │   (Port 8081)   │     │     │   (Port 8082)   │
     └────────┬────────┘     │     └────────┬────────┘
              │              │              │
              │    ┌─────────▼─────────┐    │
              │    │  Eureka Server    │    │
              │    │   (Port 8761)     │    │
              │    └───────────────────┘    │
              │                             │
     ┌────────▼────────┐           ┌────────▼────────┐
     │Enrollment Service│          │  Grade Service  │
     │   (Port 8083)   │◄─────────►│   (Port 8084)   │
     └─────────────────┘           └─────────────────┘
```

## 🛠️ Technologies Utilisées

| Technologie | Usage |
|-------------|-------|
| **Spring Boot 3.2** | Framework principal |
| **Spring Cloud** | Microservices |
| **Eureka** | Service Discovery |
| **Spring Cloud Gateway** | API Gateway |
| **OpenFeign** | Communication inter-services |
| **Resilience4J** | Circuit Breaker / Tolérance aux pannes |
| **Spring Data JPA** | Persistance |
| **H2 Database** | Base de données in-memory |
| **Lombok** | Réduction du boilerplate |

## 📁 Structure du Projet

```
mini-lms/
├── pom.xml                    # POM parent
├── eureka-server/             # Service Discovery
├── api-gateway/               # Point d'entrée unique
├── student-service/           # Gestion des étudiants
├── course-service/            # Gestion des cours
├── enrollment-service/        # Gestion des inscriptions
├── grade-service/             # Gestion des notes
├── postman_collection.json    # Tests Postman
└── README.md
```

## 🚀 Comment Démarrer

### Prérequis

- **Java 17+**
- **Maven 3.8+**
- **IntelliJ IDEA** (recommandé)

### Étapes de Démarrage

**IMPORTANT : Respectez l'ordre de démarrage !**

1. **Démarrer Eureka Server** (en premier)
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```
   Vérifiez sur : http://localhost:8761

2. **Démarrer API Gateway**
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

3. **Démarrer Student Service**
   ```bash
   cd student-service
   mvn spring-boot:run
   ```

4. **Démarrer Course Service**
   ```bash
   cd course-service
   mvn spring-boot:run
   ```

5. **Démarrer Enrollment Service**
   ```bash
   cd enrollment-service
   mvn spring-boot:run
   ```

6. **Démarrer Grade Service**
   ```bash
   cd grade-service
   mvn spring-boot:run
   ```

### Démarrage dans IntelliJ IDEA

1. Ouvrez le dossier `mini-lms` comme projet Maven
2. Attendez l'indexation
3. Créez des configurations **Run** pour chaque service
4. Démarrez dans l'ordre : Eureka → Gateway → Services métiers

## 🔌 Ports des Services

| Service | Port | URL Console H2 |
|---------|------|----------------|
| Eureka Server | 8761 | - |
| API Gateway | 8080 | - |
| Student Service | 8081 | http://localhost:8081/h2-console |
| Course Service | 8082 | http://localhost:8082/h2-console |
| Enrollment Service | 8083 | http://localhost:8083/h2-console |
| Grade Service | 8084 | http://localhost:8084/h2-console |

## 📡 Endpoints Principaux

### Via API Gateway (http://localhost:8080/api)

#### Students
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | /api/students | Créer un étudiant |
| GET | /api/students | Liste des étudiants |
| GET | /api/students/{id} | Détail d'un étudiant |
| GET | /api/students/{id}/full-profile | **Profil complet (API Synthèse)** |
| PUT | /api/students/{id} | Modifier un étudiant |
| DELETE | /api/students/{id} | Supprimer un étudiant |

#### Courses
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | /api/courses | Créer un cours |
| GET | /api/courses | Liste des cours |
| GET | /api/courses/{id} | Détail d'un cours |
| GET | /api/courses/active | Cours actifs |

#### Enrollments
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | /api/enrollments | Inscrire un étudiant |
| GET | /api/enrollments/student/{id} | Inscriptions d'un étudiant |
| GET | /api/enrollments/course/{id} | Étudiants d'un cours |

#### Grades
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | /api/grades | Attribuer une note |
| GET | /api/grades/student/{id} | Notes d'un étudiant |
| GET | /api/grades/student/{id}/statistics | Statistiques étudiant |
| GET | /api/grades/course/{id}/statistics | Statistiques cours |

## ✅ Règles de Gestion Implémentées

| Code | Règle | Implémentation |
|------|-------|----------------|
| **RG-01-INSCR** | Un étudiant ne peut s'inscrire qu'à un cours existant | Validation via appel Feign au Course Service |
| **RG-02-NOTE** | Note entre 0 et 20 | Validation avec `@DecimalMin` / `@DecimalMax` |
| **RG-03-AGREG** | Retour partiel si service HS | Fallback Resilience4J |

## 🧪 Tests avec Postman

1. Importez `postman_collection.json` dans Postman
2. Exécutez les requêtes dans l'ordre des dossiers

### Scénario de Test Complet

```
1. Créer un étudiant
2. Créer un cours
3. Inscrire l'étudiant au cours
4. Attribuer une note
5. Consulter le profil complet
```

## 🔄 Résilience (RG-03-AGREG)

Le système gère les pannes partielles :

```json
// Si Grade Service est HS, l'API retourne :
{
  "student": { ... },
  "enrollments": [ ... ],
  "grades": [],
  "gradesStatus": "indisponible",
  "enrollmentsStatus": "available"
}
```

## 📊 Exemple de Réponse - Profil Complet

```json
{
  "student": {
    "id": 1,
    "studentNumber": "STU12345678",
    "firstName": "Jean",
    "lastName": "Dupont",
    "email": "jean@email.com"
  },
  "enrollments": [
    {
      "courseCode": "MATH101",
      "courseTitle": "Mathématiques",
      "status": "ACTIVE"
    }
  ],
  "grades": [
    {
      "courseTitle": "Mathématiques",
      "value": 15.5,
      "letterGrade": "B"
    }
  ],
  "statistics": {
    "average": 15.5,
    "totalCourses": 1,
    "passRate": 100.0,
    "overallGrade": "B"
  }
}
```

## 👥 Équipe

- Développeurs Backend Spring
- Architecte Microservices

## 📝 Licence

Projet académique - 2025
