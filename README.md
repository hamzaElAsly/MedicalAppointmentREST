# Medical Appointment REST

> Web Service REST-Full — Gestion de Rendez-vous Médicaux  
> **GI8 / Module SOA**

---

## Informations du projet

| Propriété         | Valeur                          |
|-------------------|---------------------------------|
| Nom               | MedicalAppointmentREST          |
| Type              | Java Web Application (WAR)      |
| Java              | 1.8.0_202                       |
| IDE               | NetBeans IDE 8.0.2              |
| Serveur           | GlassFish 4.0.1                 |
| API REST          | JAX-RS (Jersey 2.25.1)          |
| Port              | **8085**                        |
| URL de base       | `http://localhost:8085/MedicalAppointmentREST/api` |

---

## Structure du projet

```
MedicalAppointmentREST/
│
├── pom.xml                                         ← Configuration Maven
│
└── src/
    └── main/
        ├── java/com/medical/
        │   ├── config/
        │   │   └── ApplicationConfig.java          ← @ApplicationPath("/api")
        │   │
        │   ├── model/
        │   │   ├── Patient.java                    ← POJO Patient
        │   │   ├── Medecin.java                    ← POJO Médecin
        │   │   ├── RendezVous.java                 ← POJO RendezVous (v1)
        │   │   ├── RendezVousV2.java               ← POJO RendezVous + motif (v2)
        │   │   └── ErrorResponse.java              ← { "error": "..." }
        │   │
        │   ├── exception/
        │   │   ├── MedicalException.java           ← Exception métier (Q11)
        │   │   └── MedicalExceptionMapper.java     ← Mapper → JSON (Q11)
        │   │
        │   ├── storage/
        │   │   └── DataStore.java                  ← Stockage mémoire centralisé (Q12)
        │   │
        │   ├── service/
        │   │   ├── PatientService.java             ← Logique métier patients (Q12)
        │   │   ├── MedecinService.java             ← Logique métier médecins (Q12)
        │   │   └── RendezVousService.java          ← Logique métier RDV (Q12)
        │   │
        │   ├── resource/
        │   │   ├── PatientResource.java            ← /api/patients (Q2)
        │   │   ├── MedecinResource.java            ← /api/medecins (Q3)
        │   │   ├── RendezVousResource.java         ← /api/rdv (Q1–Q10, Q13, Q16)
        │   │   └── RendezVousResourceV2.java       ← /api/v2/rdv (Q14)
        │   │
        │   └── filter/
        │       ├── SecurityFilter.java             ← X-ROLE: ADMIN (Q15)
        │       └── LoggingFilter.java              ← Log méthode + URL + heure (Q17)
        │
        └── webapp/
            └── WEB-INF/
                └── web.xml                         ← Configuration Jersey + GlassFish
```

---

## Endpoints de l'API

### Patients — `/api/patients`

| Méthode | Endpoint            | Description                          | Q  |
|---------|---------------------|--------------------------------------|----|
| POST    | `/api/patients`     | Créer un patient (email unique)      | Q2 |
| GET     | `/api/patients`     | Lister tous les patients             | Q2 |
| GET     | `/api/patients/{id}`| Récupérer un patient par ID          | Q2 |

**Exemple — Créer un patient :**
```json
POST http://localhost:8085/MedicalAppointmentREST/api/patients
Content-Type: application/json

{
  "idPatient": 1,
  "nom": "Ali Benali",
  "email": "ali.benali@email.com"
}
```

---

### Médecins — `/api/medecins`

| Méthode | Endpoint                            | Description                           | Q  |
|---------|-------------------------------------|---------------------------------------|----|
| POST    | `/api/medecins`                     | Créer un médecin                      | Q3 |
| GET     | `/api/medecins`                     | Lister tous les médecins              | Q3 |
| GET     | `/api/medecins?specialite=Cardio`   | Filtrer par spécialité                | Q3 |

**Exemple — Créer un médecin :**
```json
POST http://localhost:8085/MedicalAppointmentREST/api/medecins
Content-Type: application/json

{
  "idMedecin": 1,
  "nom": "Dr. Sara Idrissi",
  "specialite": "Cardiologie"
}
```

---

### Rendez-vous — `/api/rdv`

| Méthode | Endpoint                          | Description                                      | Q       |
|---------|-----------------------------------|--------------------------------------------------|---------|
| POST    | `/api/rdv`                        | Créer un RDV (validation + conflit + existence)  | Q1,Q4,Q5|
| GET     | `/api/rdv`                        | Lister tous les RDV                              | Socle   |
| PUT     | `/api/rdv/{id}/annuler`           | Annuler un RDV                                   | Q6      |
| PUT     | `/api/rdv/{id}/confirmer`         | Confirmer un RDV                                 | Q7      |
| GET     | `/api/rdv/medecin/{id}?page=0&size=5` | RDV d'un médecin (paginé)                    | Q8,Q13  |
| GET     | `/api/rdv/patient/{id}`           | RDV d'un patient                                 | Q9      |
| DELETE  | `/api/rdv/{id}`                   | Supprimer un RDV (impossible si CONFIRME)        | Q10     |
| GET     | `/api/rdv/stats`                  | Statistiques globales                            | Q16     |

**Exemple — Créer un rendez-vous :**
```json
POST http://localhost:8085/MedicalAppointmentREST/api/rdv
Content-Type: application/json
X-ROLE: ADMIN

{
  "idRdv": 1,
  "idPatient": 1,
  "idMedecin": 1,
  "date": "2025-06-15",
  "heure": "10:00"
}
```

**Exemple — Réponse stats :**
```json
GET http://localhost:8085/MedicalAppointmentREST/api/rdv/stats
X-ROLE: ADMIN

{
  "total": 5,
  "confirmes": 2,
  "annules": 1
}
```

---

### API v2 (avec motif) — `/api/v2/rdv`

| Méthode | Endpoint        | Description                   | Q   |
|---------|-----------------|-------------------------------|-----|
| POST    | `/api/v2/rdv`   | Créer un RDV avec motif       | Q14 |
| GET     | `/api/v2/rdv`   | Lister les RDV v2             | Q14 |

```json
POST http://localhost:8085/MedicalAppointmentREST/api/v2/rdv
Content-Type: application/json
X-ROLE: ADMIN

{
  "idRdv": 10,
  "idPatient": 1,
  "idMedecin": 1,
  "date": "2025-07-01",
  "heure": "14:00",
  "motif": "Consultation de suivi post-opératoire"
}
```

---

## Codes HTTP utilisés

| Code | Signification          | Cas d'usage                                       |
|------|------------------------|---------------------------------------------------|
| 200  | OK                     | GET réussi                                        |
| 201  | Created                | POST réussi (création)                            |
| 204  | No Content             | DELETE réussi                                     |
| 400  | Bad Request            | Données manquantes ou règle métier violée         |
| 403  | Forbidden              | Header X-ROLE manquant ou invalide (Q15)          |
| 404  | Not Found              | Patient / Médecin / RDV inexistant                |
| 409  | Conflict               | Email dupliqué (patient) ou conflit horaire (RDV) |

---

## Format d'erreur (Q11)

Toutes les erreurs retournent un JSON uniforme :

```json
{
  "error": "Description du problème"
}
```

---

## Sécurité (Q15)

Les endpoints `/api/rdv` et `/api/v2/rdv` sont protégés.  
Toutes les requêtes vers ces chemins **doivent** inclure le header :

```
X-ROLE: ADMIN
```

Sans ce header, la réponse est `403 Forbidden`.

---

## Logs (Q17)

Chaque requête HTTP est automatiquement journalisée dans la console GlassFish :

```
[LOG] 15/06/2025 10:32:45 | POST | http://localhost:8085/MedicalAppointmentREST/api/rdv
[LOG] 15/06/2025 10:33:01 | GET  | http://localhost:8085/MedicalAppointmentREST/api/rdv/stats
```

---

## Règles métier implémentées

| # | Règle                                                              |
|---|--------------------------------------------------------------------|
| Q1  | Date et heure obligatoires à la création d'un RDV               |
| Q2  | Email unique par patient ; 404 si patient inexistant             |
| Q3  | Filtre par spécialité pour les médecins                          |
| Q4  | Patient et médecin doivent exister avant création d'un RDV       |
| Q5  | Un médecin ne peut avoir deux RDV non-annulés à la même heure    |
| Q6  | Annulation impossible si RDV déjà annulé                         |
| Q7  | Confirmation impossible si RDV annulé                            |
| Q8  | Liste des RDV par médecin avec pagination                        |
| Q9  | Liste des RDV par patient                                        |
| Q10 | Suppression impossible si statut = CONFIRME                      |
| Q11 | Erreurs centralisées au format `{ "error": "..." }`              |
| Q12 | Séparation Resource / Service / DataStore                        |
| Q13 | Pagination : `?page=0&size=5`                                    |
| Q14 | API v2 `/api/v2/rdv` avec champ `motif` (v1 non cassée)         |
| Q15 | Filtre sécurité : header `X-ROLE: ADMIN` obligatoire sur /rdv   |
| Q16 | GET `/api/rdv/stats` → total, confirmés, annulés                |
| Q17 | Log automatique : méthode + URL + heure de chaque requête        |

---

## Démarrage rapide

### 1. Cloner / ouvrir dans NetBeans

Ouvrir le projet comme **Maven Project** dans NetBeans IDE 8.0.2.

### 2. Configurer GlassFish

Dans NetBeans → Services → Servers → Add Server → GlassFish Server 4.0.1  
Changer le port HTTP en **8085** dans le panneau de configuration GlassFish.

### 3. Build & Deploy

```
Clic droit sur le projet → Run (ou F6)
```

Maven compile, génère `MedicalAppointmentREST.war` et le déploie sur GlassFish.

### 4. Tester avec curl

```bash
# Créer un patient
curl -X POST http://localhost:8085/MedicalAppointmentREST/api/patients \
  -H "Content-Type: application/json" \
  -d '{"idPatient":1,"nom":"Ali Benali","email":"ali@email.com"}'

# Créer un médecin
curl -X POST http://localhost:8085/MedicalAppointmentREST/api/medecins \
  -H "Content-Type: application/json" \
  -d '{"idMedecin":1,"nom":"Dr. Idrissi","specialite":"Cardiologie"}'

# Créer un RDV (avec header sécurité)
curl -X POST http://localhost:8085/MedicalAppointmentREST/api/rdv \
  -H "Content-Type: application/json" \
  -H "X-ROLE: ADMIN" \
  -d '{"idRdv":1,"idPatient":1,"idMedecin":1,"date":"2025-06-15","heure":"10:00"}'

# Confirmer le RDV
curl -X PUT http://localhost:8085/MedicalAppointmentREST/api/rdv/1/confirmer \
  -H "X-ROLE: ADMIN"

# Statistiques
curl http://localhost:8085/MedicalAppointmentREST/api/rdv/stats \
  -H "X-ROLE: ADMIN"
```

---

## Dépendances Maven principales

| Dépendance                          | Version   | Scope    |
|-------------------------------------|-----------|----------|
| javax:javaee-api                    | 7.0       | provided |
| javax.ws.rs:javax.ws.rs-api         | 2.0.1     | provided |
| org.glassfish.jersey.core           | 2.25.1    | provided |
| jersey-media-json-jackson           | 2.25.1    | compile  |
| com.fasterxml.jackson.core          | 2.9.10    | compile  |
| javax.servlet:javax.servlet-api     | 3.1.0     | provided |

> Les dépendances `provided` sont déjà incluses dans GlassFish 4.0.1 et ne sont pas packagées dans le WAR.

---

*TP 6 — SOA | Université UPF — GI8*
