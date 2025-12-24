-- Create databases for all services
CREATE DATABASE IF NOT EXISTS exam_question_db;
CREATE DATABASE IF NOT EXISTS exam_test_db;
CREATE DATABASE IF NOT EXISTS exam_marks_db;

-- Grant permissions
GRANT ALL PRIVILEGES ON exam_question_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON exam_test_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON exam_marks_db.* TO 'root'@'%';
FLUSH PRIVILEGES;
