import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { CheckCircle2, Circle, Loader2, Send } from 'lucide-react';
import { startTest, submitAnswer, submitTest } from '../services/api';
import type { Student, Question, StartTestResponse } from '../types';

const TestInterface = () => {
    const { sessionId } = useParams();
    const navigate = useNavigate();
    const [student, setStudent] = useState<Student | null>(null);
    const [testData, setTestData] = useState<StartTestResponse | null>(null);
    const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
    const [answers, setAnswers] = useState<Map<number, string>>(new Map());
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [testSessionId, setTestSessionId] = useState<number | null>(null);

    useEffect(() => {
        const studentData = sessionStorage.getItem('student');
        const questionSetId = sessionStorage.getItem('selectedQuestionSetId');

        if (!studentData || !questionSetId) {
            navigate('/');
            return;
        }

        const parsedStudent = JSON.parse(studentData);
        setStudent(parsedStudent);
        initializeTest(parsedStudent, parseInt(questionSetId));
    }, [navigate]);

    const initializeTest = async (student: Student, questionSetId: number) => {
        try {
            setLoading(true);
            const response = await startTest({
                usn: student.usn,
                studentName: student.name,
                semester: student.semester,
                questionSetId
            });
            setTestData(response);
            setTestSessionId(response.session.id);
        } catch (err: any) {
            alert(err.response?.data?.error || 'Failed to start test');
            navigate('/test-selection');
        } finally {
            setLoading(false);
        }
    };

    const handleAnswerSelect = async (questionId: number, option: string) => {
        setAnswers(new Map(answers.set(questionId, option)));

        if (testSessionId) {
            try {
                await submitAnswer(testSessionId, { questionId, selectedOption: option });
            } catch (err) {
                console.error('Failed to save answer:', err);
            }
        }
    };

    const handleSubmitTest = async () => {
        if (!testSessionId) return;

        const unanswered = testData?.questionSet.questions.filter(q => !answers.has(q.id)).length || 0;
        if (unanswered > 0) {
            if (!confirm(`You have ${unanswered} unanswered questions. Submit anyway?`)) {
                return;
            }
        }

        try {
            setSubmitting(true);
            await submitTest(testSessionId);
            navigate(`/results/${testSessionId}`);
        } catch (err) {
            alert('Failed to submit test');
        } finally {
            setSubmitting(false);
        }
    };

    if (loading || !testData) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
            </div>
        );
    }

    const questions = testData.questionSet.questions;
    const currentQuestion = questions[currentQuestionIndex];
    const progress = ((currentQuestionIndex + 1) / questions.length) * 100;

    return (
        <div className="min-h-screen p-6">
            <div className="max-w-4xl mx-auto">
                {/* Header */}
                <div className="bg-white rounded-2xl shadow-lg p-6 mb-6">
                    <h2 className="text-2xl font-bold text-gray-900 mb-2">{testData.questionSet.name}</h2>
                    <div className="flex items-center justify-between text-sm text-gray-600">
                        <span>{student?.name} ({student?.usn})</span>
                        <span>Question {currentQuestionIndex + 1} of {questions.length}</span>
                    </div>
                    <div className="mt-4 bg-gray-200 rounded-full h-2">
                        <div
                            className="bg-blue-600 h-2 rounded-full transition-all duration-300"
                            style={{ width: `${progress}%` }}
                        />
                    </div>
                </div>

                {/* Question */}
                <div className="bg-white rounded-2xl shadow-lg p-8 mb-6">
                    <h3 className="text-xl font-semibold text-gray-900 mb-6">
                        {currentQuestion.questionText}
                    </h3>

                    <div className="space-y-3">
                        {['A', 'B', 'C', 'D'].map((option) => {
                            const optionText = currentQuestion[`option${option}` as keyof Question] as string;
                            const isSelected = answers.get(currentQuestion.id) === option;

                            return (
                                <button
                                    key={option}
                                    onClick={() => handleAnswerSelect(currentQuestion.id, option)}
                                    className={`w-full text-left p-4 rounded-lg border-2 transition-all ${isSelected
                                            ? 'border-blue-600 bg-blue-50'
                                            : 'border-gray-200 hover:border-blue-300'
                                        }`}
                                >
                                    <div className="flex items-center gap-3">
                                        {isSelected ? (
                                            <CheckCircle2 className="w-5 h-5 text-blue-600" />
                                        ) : (
                                            <Circle className="w-5 h-5 text-gray-400" />
                                        )}
                                        <span className="font-semibold text-gray-700">{option}.</span>
                                        <span className="text-gray-900">{optionText}</span>
                                    </div>
                                </button>
                            );
                        })}
                    </div>
                </div>

                {/* Navigation */}
                <div className="flex items-center justify-between">
                    <button
                        onClick={() => setCurrentQuestionIndex(Math.max(0, currentQuestionIndex - 1))}
                        disabled={currentQuestionIndex === 0}
                        className="px-6 py-3 bg-gray-200 text-gray-700 rounded-lg font-semibold disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-300 transition"
                    >
                        Previous
                    </button>

                    <div className="flex gap-2">
                        {questions.map((q, idx) => (
                            <button
                                key={q.id}
                                onClick={() => setCurrentQuestionIndex(idx)}
                                className={`w-10 h-10 rounded-lg font-semibold transition ${idx === currentQuestionIndex
                                        ? 'bg-blue-600 text-white'
                                        : answers.has(q.id)
                                            ? 'bg-green-100 text-green-700'
                                            : 'bg-gray-100 text-gray-600'
                                    }`}
                            >
                                {idx + 1}
                            </button>
                        ))}
                    </div>

                    {currentQuestionIndex === questions.length - 1 ? (
                        <button
                            onClick={handleSubmitTest}
                            disabled={submitting}
                            className="px-6 py-3 bg-green-600 text-white rounded-lg font-semibold hover:bg-green-700 transition flex items-center gap-2 disabled:opacity-50"
                        >
                            {submitting ? <Loader2 className="w-4 h-4 animate-spin" /> : <Send className="w-4 h-4" />}
                            Submit Test
                        </button>
                    ) : (
                        <button
                            onClick={() => setCurrentQuestionIndex(Math.min(questions.length - 1, currentQuestionIndex + 1))}
                            className="px-6 py-3 bg-blue-600 text-white rounded-lg font-semibold hover:bg-blue-700 transition"
                        >
                            Next
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
};

export default TestInterface;
