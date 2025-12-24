# 🎉 System is Running! - Quick Test Guide

## ✅ Services Running

All services are now running in Docker:
- **Eureka Server**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **Question Service**: http://localhost:8085
- **Test Service**: http://localhost:8086
- **Marks Service**: http://localhost:8087

## Step 1: Verify Eureka Dashboard

Open http://localhost:8761 in your browser.

Wait 1-2 minutes and you should see all services registered:
- API-GATEWAY
- QUESTION-SERVICE
- TEST-SERVICE
- MARKS-SERVICE

## Step 2: Import Postman Collection

1. Open Postman
2. Click **Import**
3. Select `postman/ExamSystem-Collection.json`
4. Collection will be imported with 25+ requests

## Step 3: Run Complete Test Workflow

### Test 1: Get Question Sets
Run: **2. Question Set Management → Get All Question Sets**

Expected: 1 question set for CS Semester 6

### Test 2: Start a Test
Run: **3. Test Session Workflow → Start Test**

This will:
- Validate USN (1MS22CS023)
- Parse department (CS) and year (22)
- Check eligibility
- Return test session and questions

### Test 3: Submit Answers
Run these in order:
1. **Submit Answer - Question 1** (Answer: B - Correct!)
2. **Submit Answer - Question 2** (Answer: C - Correct!)
3. **Submit Answer - Question 3** (Answer: A - Correct!)

### Test 4: Submit Test
Run: **Submit Test**

This triggers automatic marks calculation

### Test 5: View Results
Run: **4. Marks and Results → Get Result by Test Session**

Expected Result:
```json
{
  "totalQuestions": 3,
  "correctAnswers": 3,
  "wrongAnswers": 0,
  "score": 3,
  "percentage": 100.0
}
```

## Step 4: Test Dashboard Statistics

Run: **5. Dashboard Statistics → Get Overall Dashboard Stats**

Shows:
- Total tests taken
- Average score
- Highest/lowest scores

## Quick curl Test

```powershell
# Get question sets
curl http://localhost:8080/api/question-sets

# Start test
curl -X POST http://localhost:8080/api/test-sessions/start `
  -H "Content-Type: application/json" `
  -d '{\"usn\":\"1MS22CS023\",\"studentName\":\"John Doe\",\"semester\":6,\"questionSetId\":1}'
```

## Test Scenarios

The collection includes failure scenarios:

### Invalid Semester
Run: **6. Test Scenarios → Invalid Semester (Should Fail)**

Expected: 400 Bad Request - "Question set is for semester 6, but student is in semester 5"

### Invalid Department
Run: **6. Test Scenarios → Invalid Department (Should Fail)**

Expected: 400 Bad Request - "Question set is for department CS, but student is from department EC"

## Stopping Services

```powershell
docker-compose down
```

## Viewing Logs

```powershell
# All services
docker-compose logs -f

# Specific service
docker logs -f exam-question-service
```

## Troubleshooting

### Services not showing in Eureka
- Wait 60-90 seconds for registration
- Check logs: `docker logs exam-eureka-server`
- Restart: `docker-compose restart`

### Database errors
- Ensure MySQL is running locally
- Check databases exist:
  ```sql
  SHOW DATABASES LIKE 'exam%';
  ```

### Port conflicts
- Stop services: `docker-compose down`
- Check ports: `netstat -ano | findstr :8080`

## Success Criteria

✅ All 5 services visible in Eureka  
✅ Postman "Start Test" returns 200 OK  
✅ All 3 answers submitted successfully  
✅ Test submission triggers marks calculation  
✅ Result shows 100% (3/3 correct)  

**You're all set! The complete examination system is running!** 🚀
