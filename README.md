# Bone Health AI — Backend

The backend service for **Bone Health AI**, an application designed to support bone health assessments through AI-assisted medical image analysis and patient management.

Built with Java and Spring Boot, this service provides REST APIs for authentication, patient management, assessment records, and dashboard data.

## Features

- **Authentication:** User registration and login functionality.
- **Patient Management:** Create and retrieve patient information.
- **Assessment Management:** Create and retrieve bone health assessment records.
- **Dashboard Summary:** Provide summary data for the frontend dashboard.
- **REST API:** Expose backend functionality through HTTP endpoints.
- **Database Integration:** Persist application data through Spring Data JPA and the configured database.
- **JSON Processing:** Handle JSON request and response payloads.
- **Frontend Integration:** Provide APIs consumed by the Next.js frontend.

## Technology Stack

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Maven
- H2 Database (local development configuration)
- JSON processing

The database and dependency versions are defined in the project configuration.

## Architecture

```text
Next.js Frontend
       |
       | HTTP / JSON
       v
Spring Boot REST API
       |
       +--------------------+
       |                    |
       v                    v
 Authentication       Patient & Assessment
       |                    |
       +----------+---------+
                  |
                  v
          Spring Data JPA
                  |
                  v
              Database

Spring Boot Backend
       |
       | Prediction requests
       v
FastAPI ML Service
```

The backend handles application-related operations, while machine learning inference is handled by the separate ML service where configured.

## Prerequisites

- Java version compatible with the project's Maven configuration.
- Maven or the included Maven Wrapper, if available.
- Git.
- A configured database.
- The frontend and ML service for full application integration.

## Project Structure

```text
Bone_Health_AI_Backend/
├── .gitignore
└── Backend/
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/com/BoneHealthAI/Backend/
        │   │   ├── config/
        │   │   ├── controller/
        │   │   ├── dto/
        │   │   ├── entity/
        │   │   ├── repository/
        │   │   ├── service/
        │   │   └── BackendApplication.java
        │   └── resources/
        │       └── application.properties
        └── test/
```

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/nachiket-tonge/Bone_Health_AI_Backend.git
cd Bone_Health_AI_Backend
```

### 2. Navigate to the Spring Boot project

```bash
cd Backend
```

### 3. Configure the application

Review `src/main/resources/application.properties`.

Configure the database connection and any required application settings for your environment. Do not commit real passwords, production credentials, or private signing keys.

The application must have a valid configuration for its selected database before startup.

### 4. Run the application

Using Maven:

```bash
mvn spring-boot:run
```

Alternatively, package and run the application:

```bash
mvn package -DskipTests
java -jar target/Backend-*.jar
```

The packaging command skips test execution. It does not guarantee that the tests pass.

By default, the backend is expected to run on:

```text
http://localhost:8080
```

The exact port can be changed in the application configuration.

## API Integration

The frontend uses the backend API base URL:

```text
http://localhost:8080/api
```

Configure the frontend environment variable accordingly:

```dotenv
NEXT_PUBLIC_BACKEND_URL=http://localhost:8080/api
```

The backend includes controller functionality for authentication, patient management, and assessments, along with a dashboard summary response.

The exact HTTP methods, endpoint paths, required fields, and response formats should be verified against the controller classes in the source code.

## Database

The project uses Spring Data JPA repositories to manage persistent entities.

The local database configuration is defined in `application.properties`. If using an in-memory H2 database, data may not persist after the application stops, depending on the configured JDBC URL.

For production deployments, configure an appropriate persistent database and keep connection credentials outside committed source files.

## Testing

Run the test suite from the `Backend/` directory:

```bash
mvn test
```

To package without running tests:

```bash
mvn package -DskipTests
```

A successful application startup and a successful automated test run are separate checks. Investigate test failures before treating the test suite as passing.

## Related Repositories

- **Frontend:** [Bone_Health_AI](https://github.com/C0deHungeR/Bone_Health_AI)
- **Backend:** [Bone_Health_AI_Backend](https://github.com/nachiket-tonge/Bone_Health_AI_Backend)
- **ML Service:** [Bone_Health_AI_ML_Model](https://github.com/nachiket-tonge/Bone_Health_AI_ML_Model)

## Security Notes

- Never commit passwords, private keys, or production credentials.
- Keep signing secrets outside version control.
- Validate incoming request data.
- Use appropriate authentication and authorization for protected endpoints.
- Configure CORS and database access according to the deployment environment.

## Medical Disclaimer

Bone Health AI is intended for research and decision-support development. Its predictions must not be treated as definitive medical diagnoses. Clinical decisions should be made by qualified healthcare professionals.

## License

Add the applicable license before redistributing the project.
