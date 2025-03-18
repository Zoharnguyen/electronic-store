## Prerequisites

Before you begin, ensure you have the following installed on your system:

- **Docker** - [Installation Guide](https://docs.docker.com/get-docker/)
- **Docker Compose** - [Installation Guide](https://docs.docker.com/compose/install/)
- **Java 17** (for local development) - [Download from Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven** (for local development) - [Installation Guide](https://maven.apache.org/install.html)

## Running the Application with Docker

### Step 1: Build the JAR File

mvn clean package -DskipTests

### Step 2: Start the Docker Container

docker compose up -d --build

### Step 3: Verify the Application is Running

curl http://localhost:8080/actuator/health

## API documentation

The API documentation is available at `http://localhost:8080/swagger-ui/index.html`

## Postman Collection

The Postman collection is available at `/Postman Collection/Electronic Store APIs.postman_collection.json`.

## User Types and API Access

The application has two types of users with different access levels

### Admin User  

Roles:
- ADMIN

Permissions:
- Create, read, update and delete products
- Add discount to products

### Customer User

Roles:
- USER

Permissions:
- Create, read, update baskets
- Add products to baskets
- Remove products from baskets


