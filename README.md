# Supervision des Livraisons — 2ING INFO 2025-2026

Mobile application for monitoring deliveries, dedicated to two user types:
**controllers** and **drivers**. The backend runs in Docker, the database
persists through a named Docker volume, and the Android client follows a
strict MVVM + Room + Retrofit architecture with an offline-first driver module.

---

## 1. Project layout

```
projet_mobile/
├── backend/                    Spring Boot 3 (Java 17) REST API
│   ├── src/main/java/com/supervision/livraison/
│   │   ├── entity/             JPA entities mirroring BDG_LivraisonCom_25
│   │   ├── repository/         Spring Data JPA repositories
│   │   ├── service/            Business logic (deliveries, dashboard, auth, messages)
│   │   ├── controller/         REST endpoints under /api/**
│   │   ├── dto/                Request / response payloads
│   │   ├── config/             CORS configuration
│   │   └── SupervisionLivraisonApplication.java
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── data.sql            Seed fixture (postes, personnel, clients, articles, commandes, livraisons)
│   ├── pom.xml
│   └── Dockerfile              Multi-stage: Maven build → slim JRE runtime
├── mobile/                     Android Studio Java project (MVVM)
│   ├── app/src/main/
│   │   ├── java/com/supervision/livraison/
│   │   │   ├── api/            Retrofit interface and client
│   │   │   ├── db/             Room database + DAO (offline driver cache)
│   │   │   ├── model/          Shared DTO / entity classes
│   │   │   ├── repository/     Data access for controller (online) and driver (offline-first)
│   │   │   ├── viewmodel/      ViewModel classes (Login, Controller, Driver)
│   │   │   ├── ui/             Activities (login, controller/*, driver/*, adapters)
│   │   │   ├── LivraisonApp.java
│   │   │   └── SessionManager.java
│   │   ├── res/
│   │   │   ├── layout/         XML layouts for every screen
│   │   │   └── values/         colors, strings, themes (Material Design 3)
│   │   └── AndroidManifest.xml
│   ├── app/build.gradle
│   ├── build.gradle            Top-level
│   └── settings.gradle
├── docker-compose.yml          Two services: postgres + backend
└── README.md                   (this file)
```

---

## 2. Running the backend with Docker

### 2.1 Build and start

```bash
docker compose up --build
```

That command will:

1. Build the backend image with a **multi-stage Dockerfile**:
   - `build` stage — `maven:3.9-eclipse-temurin-17` compiles the jar (`mvn package`).
   - `runtime` stage — `eclipse-temurin:17-jre-alpine` holds only the jar + JRE,
     keeping the final image lean (~150 MB) and runs as a non-root user.
2. Start the PostgreSQL container, then the backend once the database is healthy.
3. Hibernate creates the schema; Spring loads `data.sql` to seed sample data.

API is then reachable at <http://localhost:8080/api/**>.

### 2.2 Data persistence (named Docker volume)

The `docker-compose.yml` declares a named volume:

```yaml
volumes:
  postgres_data:
services:
  postgres:
    volumes:
      - postgres_data:/var/lib/postgresql/data
```

Mounting `postgres_data` onto `/var/lib/postgresql/data` — the directory where
PostgreSQL stores every database file — means:

- `docker compose down` stops the containers but **keeps the volume**; rows
  survive restarts.
- Re-running `docker compose up` reuses the same volume, so new data added from
  the mobile app remains available.
- Only `docker compose down -v` (explicit `-v` flag) wipes the volume and
  restores the seed fixture.

### 2.3 Seed credentials

Loaded by `data.sql` on first start:

| Login  | Password | Role        |
|--------|----------|-------------|
| `ctrl1`| `pass`   | CONTROLEUR  |
| `liv1` | `pass`   | LIVREUR     |
| `liv2` | `pass`   | LIVREUR     |

Passwords are stored in clear text — fine for the academic evaluation, swap to
BCrypt for any real deployment.

---

## 3. REST API tree

| Method | Path                                        | Purpose                                           |
|--------|---------------------------------------------|---------------------------------------------------|
| POST   | `/api/auth/login`                           | Login — returns role (`CONTROLEUR` / `LIVREUR`)   |
| GET    | `/api/livraisons?from&to&etat&livreur&nocde`| Controller search with optional filters           |
| GET    | `/api/livraisons/today`                     | Controller real-time monitoring                   |
| GET    | `/api/livraisons/driver/{id}/today`         | Driver daily view                                 |
| GET    | `/api/livraisons/{nocde}`                   | Single delivery detail                            |
| PUT    | `/api/livraisons/{nocde}/etat`              | Driver updates state (+ remark for NON_LIVREE)    |
| GET    | `/api/dashboard/by-livreur`                 | Count of deliveries per driver × state            |
| GET    | `/api/dashboard/by-client`                  | Count of deliveries per client × state            |
| GET    | `/api/personnel?role=LIVREUR`               | Personnel lookup for recipient pickers            |
| GET    | `/api/personnel/{id}`                       | Profile of one staff member                       |
| POST   | `/api/messages`                             | Send a message (INFO or EMERGENCY)                |
| GET    | `/api/messages/inbox/{userId}`              | Full inbox                                        |
| GET    | `/api/messages/unread/{userId}`             | Unread inbox (polled by the mobile apps)          |
| PUT    | `/api/messages/{id}/read`                   | Mark a message as read                            |

---

## 4. Running the mobile app

### 4.1 Requirements

- Android Studio Iguana or later
- Android SDK 34, minSdk 24
- An emulator (the default `API_BASE_URL` is `http://10.0.2.2:8080/` which
  targets the host from the Android emulator)

### 4.2 Open + run

1. Open `mobile/` in Android Studio.
2. Wait for Gradle sync.
3. Run the `app` configuration on an emulator.
4. Log in with one of the seed accounts — the app routes to the controller
   or driver home based on the returned role.

### 4.3 Running on a physical device

Replace the `API_BASE_URL` build-config field in `mobile/app/build.gradle`
with your LAN IP (e.g. `http://192.168.1.20:8080/`) and rebuild.

---

## 5. Architecture notes

### 5.1 Backend — Layered architecture

```
controller   ← thin REST layer, exception translation
  ↓
service      ← business rules (e.g. remark required for NON_LIVREE, message copy of client phone)
  ↓
repository   ← Spring Data JPA
  ↓
entity       ← JPA mapping of BDG_LivraisonCom_25
```

DTOs decouple the JSON surface from the JPA graph (avoids lazy-loading issues
and stabilises the contract with the mobile client).

### 5.2 Mobile — MVVM + offline-first driver

- **View** (`Activity`) observes `LiveData` published by a **ViewModel**.
- **ViewModel** delegates to a **Repository**.
- **Controller repository** is online-only (network → `MutableLiveData`).
- **Driver repository** is offline-first: Room is the single source of truth.
  State changes are written locally (`dirty = true`) and flushed to the backend
  by `syncPending()`; this keeps the driver productive out of network coverage.

### 5.3 UX for outdoor use

- Material Design 3 DayNight theme with a high-contrast primary (#0B5FFF) and
  accent (#FFB300).
- Body text defaults to 18 sp; action buttons are 64 dp tall with 18 sp bold
  labels ("fat" buttons) so the driver can tap them while walking.
- Every delivery card shows a coloured state pill (grey/blue/green/red) for
  glance-level readability.

### 5.4 Messaging "real-time"

No websocket stack — the controller messaging screen polls
`/api/messages/inbox/{userId}` every 10 s. This keeps the backend simple and
is enough latency for the "driver on tour" scenario.

---

## 6. Tech stack (badges)

| Area | Technologies |
|------|--------------|
| Backend | ![Java](https://img.shields.io/badge/Java-ED8B00?logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white) ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?logo=spring&logoColor=white) ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?logo=hibernate&logoColor=white) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white) |
| Mobile | ![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white) ![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?logo=androidstudio&logoColor=white) ![Material 3](https://img.shields.io/badge/Material%203-757575?logo=materialdesign&logoColor=white) ![AndroidX](https://img.shields.io/badge/AndroidX-3DDC84?logo=android&logoColor=white) ![Room](https://img.shields.io/badge/Room-3DDC84?logo=android&logoColor=white) ![Retrofit](https://img.shields.io/badge/Retrofit-0095D5) ![OkHttp](https://img.shields.io/badge/OkHttp-000000) ![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white) |
| DevOps | ![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white) ![Docker Compose](https://img.shields.io/badge/Docker%20Compose-2496ED?logo=docker&logoColor=white) |
