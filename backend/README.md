# Online Examination System - Backend

A complete microservices-based examination platform with service discovery, API Gateway, and Docker support.

## Architecture

### Microservices
- **Eureka Server** (Port 8761) - Service Discovery
- **API Gateway** (Port 8080) - Single Entry Point
- **Question Service** (Port 8085) - Question Management
- **Test Service** (Port 8086) - Test Conduct
- **Marks Service** (Port 8087) - Marks Calculation

### Infrastructure
- **MySQL 8.0** - Database (Docker)
- **Docker Compose** - Container Orchestration
- **Kubernetes** - Production Deployment (Optional)

## Quick Start with Docker

### Prerequisites
- Docker and Docker Compose installed
- Java 17 (for local development)
- Maven 3.6+ (for local development)

### Step 1: Copy Microservices

Copy the three microservices from `exam-system-backend` to `backend`:

```bash
# From the project root
cp -r exam-system-backend/question-service backend/
cp -r exam-system-backend/test-service backend/
cp -r exam-system-backend/marks-service backend/
```

### Step 2: Build All Services

```bash
cd backend

# Build Eureka Server
cd eureka-server && mvn clean package -DskipTests && cd ..

# Build API Gateway
cd api-gateway && mvn clean package -DskipTests && cd ..

# Build Question Service
cd question-service && mvn clean package -DskipTests && cd ..

# Build Test Service
cd test-service && mvn clean package -DskipTests && cd ..

# Build Marks Service
cd marks-service && mvn clean package -DskipTests && cd ..
```

### Step 3: Start with Docker Compose

```bash
docker-compose up -d
```

This will start:
- MySQL database
- Eureka Server
- API Gateway
- All three microservices

### Step 4: Verify Services

- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080
- Question Service: http://localhost:8085
- Test Service: http://localhost:8086
- Marks Service: http://localhost:8087

Wait 2-3 minutes for all services to register with Eureka.

### Step 5: Test via API Gateway

All requests should go through the API Gateway:

```bash
# Get question sets
curl http://localhost:8080/api/question-sets

# Start a test
curl -X POST http://localhost:8080/api/test-sessions/start \
  -H "Content-Type: application/json" \
  -d '{
    "usn": "1MS22CS023",
    "studentName": "John Doe",
    "semester": 6,
    "questionSetId": 1
  }'
```

## Project Structure

```
backend/
├── eureka-server/          # Service Registry
├── api-gateway/            # API Gateway
├── question-service/       # Question Management
├── test-service/           # Test Conduct
├── marks-service/          # Marks Calculation
├── kubernetes/             # Kubernetes manifests
├── postman/                # Postman collection
├── docker-compose.yml      # Docker Compose config
└── init-db.sql            # MySQL initialization
```

## Service Communication

### Via Eureka (Service Discovery)
Services register with Eureka and discover each other dynamically.

### Via API Gateway
All external requests go through the API Gateway which routes to appropriate services.

### Direct Communication
Services communicate directly using RestTemplate with Eureka-based service discovery.

## Environment Variables

### Docker Compose
All environment variables are configured in `docker-compose.yml`.

### Local Development
Update `application.yml` in each service:
- Database URL
- Eureka Server URL
- Service URLs

## Kubernetes Deployment

See `kubernetes/README.md` for Kubernetes deployment instructions.

## Postman Collection

Import `postman/ExamSystem-Collection.json` for complete API testing workflow.

## Troubleshooting

### Services not registering with Eureka
- Wait 30-60 seconds after starting Eureka
- Check Eureka dashboard at http://localhost:8761
- Verify network connectivity between containers

### Database connection errors
- Ensure MySQL container is healthy: `docker-compose ps`
- Check database credentials
- Verify databases are created

### Port conflicts
- Stop any services running on ports 8080, 8085-8087, 8761
- Or change ports in docker-compose.yml

## Stopping Services

```bash
docker-compose down
```

To remove volumes (database data):
```bash
docker-compose down -v
```

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring Cloud**: 2023.0.0
- **MySQL**: 8.0
- **Docker**: Latest
- **Kubernetes**: 1.28+

## Next Steps

1. Import Postman collection for testing
2. Deploy to Kubernetes (optional)
3. Set up frontend application
4. Configure monitoring and logging

## License

MIT License
