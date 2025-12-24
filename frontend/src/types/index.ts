// Student and Test Types
export interface Student {
    usn: string;
    name: string;
    semester: number;
    department: string;
    yearOfAdmission: string;
}

export interface Question {
    id: number;
    questionText: string;
    optionA: string;
    optionB: string;
    optionC: string;
    optionD: string;
    correctOption?: string; // Only shown in results
    semester: number;
    department: string;
}

export interface QuestionSet {
    id: number;
    name: string;
    description: string;
    semester: number;
    department: string;
    questions: Question[];
}

export interface TestSession {
    id: number;
    usn: string;
    studentName: string;
    semester: number;
    department: string;
    yearOfAdmission: string;
    questionSetId: number;
    startTime: string;
    endTime?: string;
    answers: string; // JSON string
    status: 'IN_PROGRESS' | 'COMPLETED';
    submittedAt?: string;
}

export interface Result {
    id: number;
    testSessionId: number;
    usn: string;
    studentName: string;
    semester: number;
    department: string;
    questionSetId: number;
    totalQuestions: number;
    correctAnswers: number;
    wrongAnswers: number;
    score: number;
    percentage: number;
    createdAt: string;
}

export interface DashboardStats {
    totalTests: number;
    averageScore: number;
    averagePercentage: number;
    highestScore: number;
    lowestScore: number;
}

// Request Types
export interface StartTestRequest {
    usn: string;
    studentName: string;
    semester: number;
    questionSetId: number;
}

export interface SubmitAnswerRequest {
    questionId: number;
    selectedOption: string;
}

// Response Types
export interface StartTestResponse {
    session: TestSession;
    questionSet: QuestionSet;
}
