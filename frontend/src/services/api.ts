import axios from 'axios';
import type {
    QuestionSet,
    StartTestRequest,
    StartTestResponse,
    SubmitAnswerRequest,
    TestSession,
    Result,
    DashboardStats
} from '../types';

const API_BASE_URL = 'http://localhost:8080'; // API Gateway

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Question Sets
export const getQuestionSets = async (semester: number, department: string): Promise<QuestionSet[]> => {
    const response = await api.get(`/api/question-sets`, {
        params: { semester, department }
    });
    return response.data;
};

export const getQuestionSetById = async (id: number): Promise<QuestionSet> => {
    const response = await api.get(`/api/question-sets/${id}`);
    return response.data;
};

// Test Sessions
export const startTest = async (data: StartTestRequest): Promise<StartTestResponse> => {
    const response = await api.post('/api/test-sessions/start', data);
    return response.data;
};

export const submitAnswer = async (sessionId: number, answer: SubmitAnswerRequest): Promise<TestSession> => {
    const response = await api.put(`/api/test-sessions/${sessionId}/answer`, answer);
    return response.data;
};

export const submitTest = async (sessionId: number): Promise<TestSession> => {
    const response = await api.post(`/api/test-sessions/${sessionId}/submit`);
    return response.data;
};

export const getTestSession = async (sessionId: number): Promise<TestSession> => {
    const response = await api.get(`/api/test-sessions/${sessionId}`);
    return response.data;
};

// Results
export const getResultBySessionId = async (sessionId: number): Promise<Result> => {
    const response = await api.get(`/api/marks/test-session/${sessionId}`);
    return response.data;
};

export const getResultsByUsn = async (usn: string): Promise<Result[]> => {
    const response = await api.get(`/api/marks/usn/${usn}`);
    return response.data;
};

// Dashboard
export const getDashboardStats = async (): Promise<DashboardStats> => {
    const response = await api.get('/api/marks/dashboard');
    return response.data;
};

export const getDashboardStatsByUsn = async (usn: string): Promise<DashboardStats> => {
    const response = await api.get(`/api/marks/dashboard/usn/${usn}`);
    return response.data;
};
