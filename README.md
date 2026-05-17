# Azar Cafetero - Authentication Service

Welcome to the **Auth Service** repository for Azar Cafetero. This microservice is a critical component of the platform, acting as the centralized authority for player authentication, profile management, and virtual economy operations. It ensures that only verified users can access the game platform and interact with their in-game assets.

## 🚀 Technology Stack

- **[Java & Spring Boot](https://spring.io/projects/spring-boot)**: The robust core framework providing a production-ready REST API. It handles dependency injection, security filtering, and web request mapping.
- **Spring Data JPA & Hibernate**: Used for reliable persistence of player profiles, statistics, and transaction histories in the underlying database.
- **Spring Security & OAuth2**: Deeply integrated with Google OAuth 2.0 to securely validate client-side Google identity tokens without requiring players to maintain separate passwords.
- **[Maven](https://maven.apache.org/)**: Dependency management and build lifecycle orchestration.
- **[Docker](https://www.docker.com/)**: Containerized for consistent, environment-agnostic deployment across development, staging, and production.
- **SonarQube**: Configured to ensure continuous code quality, detect code smells, and enforce security vulnerability scanning via the `sonar-project.properties` configuration.

## 🛠️ Architecture & Responsibilities

This service handles the lifecycle of a player's identity and wallet:

### 1. Authentication Flow
When a player signs in via the Next.js frontend using the `@react-oauth/google` library, a Google ID token is generated. This token is securely transmitted to the Auth Service, where it is cryptographically verified against Google's public keys. Upon successful validation, the service either creates a new player profile or retrieves an existing one.

### 2. Player Profiles & State
Manages the `Player` domain entity, which includes:
- Unique player identifiers tied to their Google account.
- Usernames and avatar data.
- Win/loss statistics across various games (Brisca, Parqués).

### 3. Balance & Economy Management
Provides secure REST endpoints to manage the player's virtual currency:
- `GET /player/balance`: Retrieves the current static balance.
- `GET /player/balance/live`: Provides real-time or updated balance checks for immediate UI synchronization.
- **Security Check**: These endpoints strictly require a valid, authenticated session, rejecting unauthorized access with `401 Unauthorized`.

## 🏃‍♂️ Getting Started

### Prerequisites
- Java 17+ (JDK)
- Maven 3.8+
- An active database instance (e.g., PostgreSQL or MySQL) configured in `application.properties`.
- Google OAuth Client ID and Secret configured as environment variables.

### Running Locally

You can run the application seamlessly using the included Maven wrapper:

```bash
./mvnw spring-boot:run
```

### Docker Deployment

To build and run the Docker container locally:

```bash
docker build -t azarcafetero-auth .
docker run -p 8080:8080 --env-file .env azarcafetero-auth
```

## 🧪 Testing & Code Quality

The repository includes a suite of unit and integration tests covering the security configurations and domain logic.

Run tests using Maven:
```bash
./mvnw test
```

For static code analysis with SonarQube (requires a running Sonar server):
```bash
./mvnw sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=YOUR_TOKEN
```
