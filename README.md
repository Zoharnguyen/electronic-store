# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.altech.electronic-store' is invalid and this project uses 'com.altech.electronic_store' instead.

# Prerequisites

- Docker
- Maven
- Java 17

# Getting Started

1. Run `docker compose up -d --build` to start the application.
2. Run `docker compose down` to stop the application.

# API Documentation

The API documentation is available at `http://localhost:8080/swagger-ui/index.html`.

# Health Check

The health check is available at `http://localhost:8080/actuator/health`.

# H2 Console

The H2 Console is available at `http://localhost:8080/h2-console`.

# Electronic Store API

This document provides instructions for setting up and running the Electronic Store API application.

## Prerequisites

Before you begin, ensure you have the following installed on your system:

- **Docker** - [Installation Guide](https://docs.docker.com/get-docker/)
- **Docker Compose** - [Installation Guide](https://docs.docker.com/compose/install/)
- **Java 17** (for local development) - [Download from Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven** (for local development) - [Installation Guide](https://maven.apache.org/install.html)

## Running the Application with Docker

### Step 1: Build the JAR File

./mvnw clean package -DskipTests

### Step 2: Start the Docker Container

docker compose up -d --build

### Step 3: Verify the Application is Running

curl http://localhost:8080/actuator/health

## User Types and API Access

The application has two types of users with different access levels



