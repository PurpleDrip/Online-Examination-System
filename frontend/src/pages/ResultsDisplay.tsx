import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Trophy, CheckCircle, XCircle, Home, Loader2, BarChart3 } from 'lucide-react';
import { getResultBySessionId } from '../services/api';
import type { Result } from '../types';

const ResultsDisplay = () => {
    const { sessionId } = useParams();
    const navigate = useNavigate();
    const [result, setResult] = useState<Result | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (sessionId) {
            fetchResult(parseInt(sessionId));
        }
    }, [sessionId]);

    const fetchResult = async (id: number) => {
        try {
            setLoading(true);
            const data = await getResultBySessionId(id);
            setResult(data);
        } catch (err) {
            console.error('Failed to fetch result:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleNewTest = () => {
        sessionStorage.clear();
        navigate('/');
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
            </div>
        );
    }

    if (!result) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="text-center">
                    <p className="text-gray-600 mb-4">Result not found</p>
                    <button onClick={handleNewTest} className="px-6 py-3 bg-blue-600 text-white rounded-lg">
                        Go Home
                    </button>
                </div>
            </div>
        );
    }

    const getGrade = (percentage: number) => {
        if (percentage >= 90) return { grade: 'A+', color: 'text-green-600' };
        if (percentage >= 80) return { grade: 'A', color: 'text-green-500' };
        if (percentage >= 70) return { grade: 'B', color: 'text-blue-600' };
        if (percentage >= 60) return { grade: 'C', color: 'text-yellow-600' };
        if (percentage >= 50) return { grade: 'D', color: 'text-orange-600' };
        return { grade: 'F', color: 'text-red-600' };
    };

    const gradeInfo = getGrade(result.percentage);

    return (
        <div className="min-h-screen p-6">
            <div className="max-w-2xl mx-auto">
                {/* Success Header */}
                <div className="text-center mb-8">
                    <div className="inline-flex items-center justify-center w-20 h-20 bg-green-100 rounded-full mb-4">
                        <Trophy className="w-10 h-10 text-green-600" />
                    </div>
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">Test Completed!</h1>
                    <p className="text-gray-600">Here are your results</p>
                </div>

                {/* Score Card */}
                <div className="bg-white rounded-2xl shadow-2xl p-8 mb-6">
                    <div className="text-center mb-8">
                        <div className={`text-6xl font-bold mb-2 ${gradeInfo.color}`}>
                            {result.percentage.toFixed(1)}%
                        </div>
                        <div className={`text-3xl font-bold ${gradeInfo.color}`}>
                            Grade: {gradeInfo.grade}
                        </div>
                    </div>

                    <div className="grid grid-cols-3 gap-4 mb-6">
                        <div className="text-center p-4 bg-blue-50 rounded-lg">
                            <div className="text-2xl font-bold text-blue-600">{result.totalQuestions}</div>
                            <div className="text-sm text-gray-600">Total Questions</div>
                        </div>
                        <div className="text-center p-4 bg-green-50 rounded-lg">
                            <div className="text-2xl font-bold text-green-600">{result.correctAnswers}</div>
                            <div className="text-sm text-gray-600">Correct</div>
                        </div>
                        <div className="text-center p-4 bg-red-50 rounded-lg">
                            <div className="text-2xl font-bold text-red-600">{result.wrongAnswers}</div>
                            <div className="text-sm text-gray-600">Wrong</div>
                        </div>
                    </div>

                    <div className="space-y-3">
                        <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                            <span className="text-gray-700">Student Name</span>
                            <span className="font-semibold">{result.studentName}</span>
                        </div>
                        <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                            <span className="text-gray-700">USN</span>
                            <span className="font-semibold">{result.usn}</span>
                        </div>
                        <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                            <span className="text-gray-700">Semester</span>
                            <span className="font-semibold">{result.semester}</span>
                        </div>
                        <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                            <span className="text-gray-700">Department</span>
                            <span className="font-semibold">{result.department}</span>
                        </div>
                    </div>
                </div>

                {/* Actions */}
                <div className="flex gap-4">
                    <button
                        onClick={() => navigate('/dashboard')}
                        className="flex-1 bg-purple-600 hover:bg-purple-700 text-white font-semibold py-4 px-6 rounded-lg transition flex items-center justify-center gap-2"
                    >
                        <BarChart3 className="w-5 h-5" />
                        View Dashboard
                    </button>
                    <button
                        onClick={handleNewTest}
                        className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-4 px-6 rounded-lg transition flex items-center justify-center gap-2"
                    >
                        <Home className="w-5 h-5" />
                        Take Another Test
                    </button>
                </div>

                {/* Performance Message */}
                <div className="mt-6 p-6 bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg">
                    <p className="text-center text-gray-700">
                        {result.percentage >= 80 ? (
                            <>
                                <CheckCircle className="w-6 h-6 text-green-600 inline mr-2" />
                                <strong>Excellent work!</strong> You've demonstrated strong understanding.
                            </>
                        ) : result.percentage >= 60 ? (
                            <>
                                <CheckCircle className="w-6 h-6 text-blue-600 inline mr-2" />
                                <strong>Good job!</strong> Keep practicing to improve further.
                            </>
                        ) : (
                            <>
                                <XCircle className="w-6 h-6 text-orange-600 inline mr-2" />
                                <strong>Keep trying!</strong> Review the material and try again.
                            </>
                        )}
                    </p>
                </div>
            </div>
        </div>
    );
};

export default ResultsDisplay;
