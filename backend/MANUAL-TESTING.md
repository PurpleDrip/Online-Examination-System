# Manual Testing Guide (Without Docker)

Since Docker is having issues, let's test the system manually first.

## Prerequisites

- Java 17
- Maven
- MySQL 8.0 running on localhost:3306
- MySQL root password: root

## Step 1: Create Databases

Run this in MySQL:

```sql
CREATE DATABASE IF NOT EXISTS exam_question_db;
CREATE DATABASE IF NOT EXISTS exam_test_db;
CREATE DATABASE IF NOT EXISTS exam_marks_db;
```

## Step 2: Start Services (5 Separate Terminals)

### Terminal 1 - Eureka Server
```powershell
cd backend\eureka-server
mvn spring-boot:run
```

Wait until you see: "Started EurekaServerApplication"

### Terminal 2 - API Gateway (Wait 30 seconds after Eureka)
```powershell
cd backend\api-gateway
mvn spring-boot:run
```

Wait until you see: "Started ApiGatewayApplication"

### Terminal 3 - Question Service
```powershell
cd backend\question-service
mvn spring-boot:run
```

Wait until you see: "Started QuestionServiceApplication" and "Created 3 hardcoded questions"

### Terminal 4 - Test Service
```powershell
cd backend\test-service
mvn spring-boot:run
```

Wait until you see: "Started TestServiceApplication"

### Terminal 5 - Marks Service
```powershell
cd backend\marks-service
mvn spring-boot:run
```

Wait until you see: "Started MarksServiceApplication"

## Step 3: Verify Services

Open browser to http://localhost:8761

You should see all 5 services registered:
- API-GATEWAY
- QUESTION-SERVICE
- TEST-SERVICE
- MARKS-SERVICE

## Step 4: Test with Postman

1. Import `postman/ExamSystem-Collection.json`
2. Run "Get All Question Sets" - Should return 1 question set
3. Run "Start Test" - Should create a test session
4. Run "Submit Answer" requests (3x)
5. Run "Submit Test"
6. Run "Get Result by Test Session" - Should show 100% (3/3)

## Quick Test with curl

```powershell
# Get question sets
curl http://localhost:8080/api/question-sets

# Start test
curl -X POST http://localhost:8080/api/test-sessions/start `
  -H "Content-Type: application/json" `
  -d '{\"usn\": \"1MS22CS023\", \"studentName\": \"John Doe\", \"semester\": 6, \"questionSetId\": 1}'

# Submit answers (replace {sessionId} with actual ID)
curl -X PUT http://localhost:8080/api/test-sessions/1/answer `
  -H "Content-Type: application/json" `
  -d '{\"questionId\": 1, \"selectedOption\": \"B\"}'

curl -X PUT http://localhost:8080/api/test-sessions/1/answer `
  -H "Content-Type: application/json" `
  -d '{\"questionId\": 2, \"selectedOption\": \"C\"}'

curl -X PUT http://localhost:8080/api/test-sessions/1/answer `
  -H "Content-Type: application/json" `
  -d '{\"questionId\": 3, \"selectedOption\": \"A\"}'

# Submit test
curl -X POST http://localhost:8080/api/test-sessions/1/submit

# Get results
curl http://localhost:8080/api/marks/test-session/1
```

## Expected Results

All 3 answers are correct, so you should get:
- Total Questions: 3
- Correct Answers: 3
- Score: 3
- Percentage: 100.0

## Troubleshooting

### Port Already in Use
```powershell
# Find process using port
netstat -ano | findstr :8085
# Kill process
taskkill /PID <process_id> /F
```

### Database Connection Error
- Verify MySQL is running
- Check username/password in application.yml
- Ensure databases are created

### Services Not Registering with Eureka
- Wait 30-60 seconds
- Check Eureka dashboard at http://localhost:8761
- Restart the service

## Docker Issues

If you want to fix Docker later:

1. Make sure Docker Desktop is running
2. Try: `docker system prune -a` (WARNING: Removes all images)
3. Restart Docker Desktop
4. Try `docker-compose up -d` again

For now, manual testing works perfectly!
