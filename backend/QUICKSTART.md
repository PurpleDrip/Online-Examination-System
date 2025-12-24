# Online Examination System - Quick Start

Get the complete system running in minutes!

## Option 1: Docker Compose (Recommended)

### Prerequisites
- Docker and Docker Compose
- 8GB RAM minimum

### Steps

1. **Navigate to backend folder**
   ```bash
   cd backend
   ```

2. **Build all services**
   ```bash
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

3. **Start everything with Docker Compose**
   ```bash
   docker-compose up -d
   ```

4. **Wait for services to start** (2-3 minutes)
   ```bash
   docker-compose logs -f
   ```

5. **Verify services**
   - Eureka Dashboard: http://localhost:8761
   - API Gateway: http://localhost:8080
   - All services should show as registered in Eureka

6. **Test the system**
   - Import Postman collection from `postman/ExamSystem-Collection.json`
   - Run the "Get All Question Sets" request
   - Run the complete test workflow

## Option 2: Local Development

### Prerequisites
- Java 17
- Maven 3.6+
- MySQL 8.0 running locally

### Steps

1. **Create MySQL databases**
   ```sql
   CREATE DATABASE exam_question_db;
   CREATE DATABASE exam_test_db;
   CREATE DATABASE exam_marks_db;
   ```

2. **Start services in order** (separate terminals)
   ```bash
   # Terminal 1 - Eureka Server
   cd eureka-server && mvn spring-boot:run
   
   # Terminal 2 - API Gateway (wait 30s after Eureka)
   cd api-gateway && mvn spring-boot:run
   
   # Terminal 3 - Question Service
   cd question-service && mvn spring-boot:run
   
   # Terminal 4 - Test Service
   cd test-service && mvn spring-boot:run
   
   # Terminal 5 - Marks Service
   cd marks-service && mvn spring-boot:run
   ```

3. **Test the system** (same as Docker option)

## Quick Test Workflow

### 1. Get Available Question Sets
```bash
curl http://localhost:8080/api/question-sets
```

### 2. Start a Test
```bash
curl -X POST http://localhost:8080/api/test-sessions/start \
  -H "Content-Type: application/json" \
  -d '{
    "usn": "1MS22CS023",
    "studentName": "John Doe",
    "semester": 6,
    "questionSetId": 1
  }'
```

Save the `session.id` from the response.

### 3. Submit Answers
```bash
# Question 1 (Correct: B)
curl -X PUT http://localhost:8080/api/test-sessions/1/answer \
  -H "Content-Type: application/json" \
  -d '{"questionId": 1, "selectedOption": "B"}'

# Question 2 (Correct: C)
curl -X PUT http://localhost:8080/api/test-sessions/1/answer \
  -H "Content-Type: application/json" \
  -d '{"questionId": 2, "selectedOption": "C"}'

# Question 3 (Correct: A)
curl -X PUT http://localhost:8080/api/test-sessions/1/answer \
  -H "Content-Type: application/json" \
  -d '{"questionId": 3, "selectedOption": "A"}'
```

### 4. Submit Test
```bash
curl -X POST http://localhost:8080/api/test-sessions/1/submit
```

### 5. View Results
```bash
curl http://localhost:8080/api/marks/test-session/1
```

Expected result: 3/3 (100%)

## Postman Collection

1. Open Postman
2. Import `postman/ExamSystem-Collection.json`
3. Collection includes:
   - Question Management (6 requests)
   - Question Set Management (3 requests)
   - Test Session Workflow (7 requests)
   - Marks and Results (3 requests)
   - Dashboard Statistics (4 requests)
   - Test Scenarios (2 requests)

4. Run requests in order or use the Runner for automated testing

## Hardcoded Test Data

The system includes 3 questions for CS Department, Semester 6:

| Question | Correct Answer |
|----------|----------------|
| What is the time complexity of binary search? | B (O(log n)) |
| Which data structure uses LIFO? | C (Stack) |
| What does SQL stand for? | A (Structured Query Language) |

## Troubleshooting

### Docker Compose Issues

**Services not starting:**
```bash
docker-compose logs <service-name>
docker-compose restart <service-name>
```

**Port conflicts:**
```bash
docker-compose down
# Change ports in docker-compose.yml
docker-compose up -d
```

**Clean restart:**
```bash
docker-compose down -v
docker-compose up -d --build
```

### Local Development Issues

**Port already in use:**
- Kill process or change port in application.yml

**Database connection error:**
- Verify MySQL is running
- Check credentials (default: root/root)

**Services not registering with Eureka:**
- Wait 30-60 seconds
- Check Eureka dashboard
- Verify eureka.client.serviceUrl.defaultZone

## Next Steps

- ✅ Backend is running
- ⏭️ Test with Postman collection
- ⏭️ Deploy to Kubernetes (see `kubernetes/README.md`)
- ⏭️ Set up frontend application

## Support

For detailed documentation, see:
- `README.md` - Complete documentation
- `kubernetes/README.md` - Kubernetes deployment
- Postman collection - API examples
