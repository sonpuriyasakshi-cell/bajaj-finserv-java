# Bajaj Finserv Health Qualifier - Spring Boot 3 Application

This is a complete, production-ready Spring Boot 3 application written in Java 17 for the **Bajaj Finserv Health Qualifier** task.

It is designed to automate the required hiring workflow on startup without requiring any manual REST controller trigger.

---

## 🌟 Key Features

- **Automated Workflow on Startup**: Initiates execution immediately upon startup using Spring's `CommandLineRunner`.
- **Reactive HTTP Client (`WebClient`)**: Uses Spring WebFlux's modern, non-blocking `WebClient` for high performance instead of the deprecated `RestTemplate`.
- **Dynamic Question Logic**: Automatically parses your registration number's digits to determine whether to execute **Question 1 SQL (Odd)** or **Question 2 SQL (Even)**.
- **Robust Error Handling**: Comprehensive try-catch blocks and explicit log outputs indicating exactly where errors occurred.
- **Production-Ready Docker Setup**: Multi-stage `Dockerfile` to produce minimal production images using `eclipse-temurin:17-jdk`.
- **Render blueprint deployment**: Cloud-ready configuration via `render.yaml`.

---

## 🏗️ Project Structure

```text
bajaj-finserv-qualifier/
├── src/
│   └── main/
│       ├── java/com/bajaj/qualifier/
│       │   ├── BajajFinservQualifierApplication.java  # Bootstrap Application Class
│       │   ├── config/
│       │   │   └── WebClientConfig.java               # Configures WebClient with timeouts
│       │   ├── constant/
│       │   │   └── ChallengeConstants.java            # Holds target endpoints & SQL Queries
│       │   ├── dto/
│       │   │   ├── QuerySubmissionRequest.java        # Solution submission DTO
│       │   │   ├── WebhookRequest.java                # Webhook generation Request DTO
│       │   │   └── WebhookResponse.java               # Webhook generation Response DTO
│       │   ├── runner/
│       │   │   └── StartupRunner.java                 # CommandLineRunner hook
│       │   └── service/
│       │       └── ChallengeService.java              # Logic controller & API Orchestrator
│       └── resources/
│           └── application.yml                        # App configuration properties
├── Dockerfile                                         # Multi-stage production container setup
├── docker-compose.yml                                 # Orchestrates application running locally
├── render.yaml                                        # Render blueprint configuration
├── pom.xml                                            # Maven dependencies
├── .gitignore                                         # Version control ignore lists
└── README.md                                          # This documentation
```

---

## 🔧 Setup & Configuration

You can configure the application details by updating the `src/main/resources/application.yml` file:

```yaml
bajaj:
  challenge:
    name: "Sakshi Sonpuriya"
    regNo: "YOUR_REG_NO" # <-- Enter your registration number here (e.g. REG12347)
    email: "YOUR_EMAIL"  # <-- Enter your email here
```

Alternatively, you can supply them as **Environment Variables** at runtime:
* `BAJAJ_REG_NO`
* `BAJAJ_EMAIL`
* `BAJAJ_NAME`

---

## 🚀 Running Locally

### Prerequisites
* Java 17 JDK installed
* Maven installed (or use the provided Maven Wrapper `./mvnw`)

### 1. Build the Application
Run the Maven package command to compile the code and build the JAR artifact:
```bash
./mvnw clean package
```

### 2. Run the JAR
Execute the packaged JAR directly:
```bash
java -jar target/bajaj-finserv-qualifier-0.0.1-SNAPSHOT.jar
```
Or run the Spring Boot plugin goal:
```bash
./mvnw spring-boot:run
```

Check the console output logs. You will see detailed workflow execution steps:
1. Sending candidate details to `/hiring/generateWebhook/JAVA`.
2. Extracting `webhook` URL and `accessToken` (JWT).
3. Analyzing registration number (checking odd/even ending digits).
4. Submitting the selected SQL query to the returned webhook URL.

---

## 🐳 Docker Deployment

A multi-stage Docker build packages the application and exposes it on port `8080`.

### Build Docker Image
```bash
docker build -t bajaj-finserv-qualifier .
```

### Run Container (Injecting Env Vars)
```bash
docker run -p 8080:8080 \
  -e BAJAJ_REG_NO="YOUR_REG_NO" \
  -e BAJAJ_EMAIL="YOUR_EMAIL" \
  bajaj-finserv-qualifier
```

### Run with Docker Compose
Simply modify the environment variables inside `docker-compose.yml` and execute:
```bash
docker-compose up --build
```

---

## ☁️ Render Deployment

Deploying the service to **Render** is automated using the Blueprint specification (`render.yaml`).

### Setup Blueprint on Render:
1. Push your repository to your GitHub account.
2. In the Render Dashboard, click **New +** and select **Blueprint**.
3. Connect your GitHub repository.
4. Render will automatically read `render.yaml` to set up:
   * Build Command: `./mvnw clean package -DskipTests`
   * Start Command: `java -jar target/bajaj-finserv-qualifier-0.0.1-SNAPSHOT.jar`
5. Make sure to define the environment variables `BAJAJ_REG_NO` and `BAJAJ_EMAIL` in Render's dashboard.

---

## 🐙 Push to GitHub

To quickly publish your solution onto GitHub, run these commands from the root directory:

```bash
# Initialize local git repository
git init

# Add all files to stage
git add .

# Create initial commit
git commit -m "Initial commit: Complete Bajaj Finserv Qualifier solution"

# Add your remote origin repository URL (Replace with your actual repo link)
git remote add origin https://github.com/your-username/bajaj-finserv-qualifier.git

# Set main branch and push
git branch -M main
git push -u origin main
```
