# Bajaj Finserv Health Qualifier — Spring Boot 3

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)]()
[![License](https://img.shields.io/badge/license-MIT-lightgrey)]()

---

## 📋 Project Overview

This is a **complete, production-ready automation application** built with Spring Boot 3 and Java 17 for the Bajaj Finserv Health hiring qualifier challenge.

It is **NOT a web server**. It runs as a pure command-line automation program that:
1. Starts automatically via `CommandLineRunner` (no REST endpoints, no port binding)
2. Calls the Bajaj Finserv Health API to generate a webhook
3. Detects the correct SQL question based on the registration number's last two digits
4. Submits the final SQL query to the generated webhook URL using JWT authorization
5. Exits cleanly with `System.exit(0)` on success

---

## 🔄 Webhook Flow

```
App Startup
    │
    ▼
CommandLineRunner (StartupRunner)
    │
    ▼
POST /hiring/generateWebhook/JAVA
Body: { name, regNo, email }
    │
    ▼
Response: { webhook, accessToken }
    │
    ▼
Check last 2 digits of regNo
    ├── ODD  → Use QUESTION 1 SQL
    └── EVEN → Use QUESTION 2 SQL
    │
    ▼
POST <webhook_url>
Headers: Authorization: <accessToken>
Body: { "finalQuery": "<selected SQL>" }
    │
    ▼
{"success":true,"message":"Webhook processed successfully"}
    │
    ▼
System.exit(0) — Clean Shutdown
```

---

## ⚡ Startup Automation

The application triggers its full workflow **on startup** using Spring's `CommandLineRunner` interface. This means:
- **No HTTP endpoints** need to be called manually
- **No REST controllers** exist in this project
- `SpringApplication` is configured with `WebApplicationType.NONE` to **prevent any web server from starting**
- **No port binding** occurs — `8080` is never touched

---

## 🧰 Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Language runtime |
| Spring Boot | 3.2.5 | Application framework |
| Spring WebFlux | 6.x | WebClient for async HTTP calls |
| Project Lombok | Latest | Boilerplate reduction |
| Jackson | 2.x | JSON serialization |
| Maven | 3.9.6 | Build tool |

---

## 🏗️ Project Structure

```text
bajaj-finserv-qualifier/
├── src/
│   └── main/
│       ├── java/com/bajaj/qualifier/
│       │   ├── BajajFinservQualifierApplication.java  ← Main class (WebApplicationType.NONE)
│       │   ├── config/
│       │   │   └── WebClientConfig.java               ← WebClient bean with timeouts
│       │   ├── constant/
│       │   │   └── ChallengeConstants.java            ← API path + SQL queries
│       │   ├── dto/
│       │   │   ├── WebhookRequest.java                ← Candidate details payload
│       │   │   ├── WebhookResponse.java               ← Webhook URL + JWT token
│       │   │   └── QuerySubmissionRequest.java        ← Final SQL submission payload
│       │   ├── runner/
│       │   │   └── StartupRunner.java                 ← CommandLineRunner trigger
│       │   └── service/
│       │       └── ChallengeService.java              ← Full orchestration logic
│       └── resources/
│           └── application.yml                        ← Candidate configuration
├── Dockerfile                                         ← Multi-stage container build
├── docker-compose.yml                                 ← Local container orchestration
├── render.yaml                                        ← Render deployment blueprint
├── mvnw / mvnw.cmd                                    ← Maven wrapper scripts
├── .mvn/wrapper/                                      ← Maven wrapper config
├── pom.xml                                            ← Maven dependencies
├── .gitignore                                         ← Version control rules
└── README.md                                          ← This documentation
```

---

## 🔧 Configuration

Update `src/main/resources/application.yml`:

```yaml
bajaj:
  challenge:
    name: Sakshi Sonpuriya
    regNo: 0827AL231108         # ← Your registration number
    email: sonpuriyasakshi@gmail.com  # ← Your email

logging:
  level:
    root: INFO
```

---

## 🚀 Build Instructions

### Prerequisites
- Java 17+ installed
- Internet access (to download Maven deps on first run)

### Build the JAR
```bash
./mvnw clean package
```
> On Windows: `.\mvnw.cmd clean package`

Expected output:
```
[INFO] BUILD SUCCESS
[INFO] Building jar: target/bajaj-finserv-qualifier-0.0.1-SNAPSHOT.jar
[INFO] Total time: ~15s
```

---

## ▶️ Local Run Instructions

### Option 1 — Maven Spring Boot Run (recommended)
```bash
./mvnw spring-boot:run
```
> On Windows: `.\mvnw.cmd spring-boot:run`

### Option 2 — Run the built JAR directly
```bash
java -jar target/bajaj-finserv-qualifier-0.0.1-SNAPSHOT.jar
```

### Expected Output
```
=================================================================
Starting Bajaj Finserv Qualifier Challenge Workflow...
=================================================================
Step 1: Requesting Webhook & Access Token from: https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA
Step 1 SUCCESS! Details retrieved:
-> Returned Webhook URL: https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA
-> Access Token (first 15 chars): eyJhbGciOiJIUzI...
=================================================================
QUESTION DETECTION LOGIC
Registration Number: 0827AL231108
Extracted last two digits even? -> true
Selected Question: QUESTION 2 SQL (Even)
=================================================================
Step 2: Submitting solution to Webhook URL: ...
Step 2 SUCCESS! Solution submitted successfully.
Server Response:
{"success":true,"message":"Webhook processed successfully"}
=================================================================
[INFO] BUILD SUCCESS
```

---

## 🐳 Docker Deployment

### Build Docker Image
```bash
docker build -t bajaj-finserv-qualifier .
```

### Run Container
```bash
docker run bajaj-finserv-qualifier
```

### Run with Docker Compose
```bash
docker-compose up --build
```

---

## ☁️ Render Deployment

### Using Blueprint (`render.yaml`)
1. Push this repository to GitHub
2. Login to [Render.com](https://render.com)
3. Click **New +** → **Blueprint**
4. Connect your GitHub repository
5. Render automatically reads `render.yaml`:
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/bajaj-finserv-qualifier-0.0.1-SNAPSHOT.jar`

---

## 📦 Downloadable JAR

The prebuilt JAR is available as a GitHub Release artifact:

> **Raw JAR Download**: `https://github.com/<your-username>/bajaj-finserv-qualifier/releases/download/v1.0/bajaj-finserv-qualifier-0.0.1-SNAPSHOT.jar`

*(Update link after pushing to GitHub)*

---

## 🐙 Push to GitHub

```bash
# Repository is already initialized and committed locally
# Add your GitHub remote:
git remote add origin https://github.com/<your-username>/bajaj-finserv-qualifier.git
git branch -M main
git push -u origin main
```

---

## ✅ Submission Checklist

- [x] App runs on startup automatically
- [x] No REST endpoint required to trigger flow
- [x] WebClient used instead of RestTemplate
- [x] Correct SQL selected based on regNo digits
- [x] JWT used in Authorization header
- [x] Server responds: `{"success":true,"message":"Webhook processed successfully"}`
- [x] Application exits cleanly
- [x] BUILD SUCCESS verified
