import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { BookOpen, Users, Clock, ArrowRight, Loader2 } from 'lucide-react';
import { getQuestionSets } from '../services/api';
import type { Student, QuestionSet } from '../types';

const TestSelection = () => {
    const navigate = useNavigate();
    const [student, setStudent] = useState<Student | null>(null);
    const [questionSets, setQuestionSets] = useState<QuestionSet[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const studentData = sessionStorage.getItem('student');
        if (!studentData) {
            navigate('/');
            return;
        }

        const parsedStudent = JSON.parse(studentData);
        setStudent(parsedStudent);

        fetchQuestionSets(parsedStudent.semester, parsedStudent.department);
    }, [navigate]);

    const fetchQuestionSets = async (semester: number, department: string) => {
        try {
            setLoading(true);
            const sets = await getQuestionSets(semester, department);
            setQuestionSets(sets);
        } catch (err) {
            setError('Failed to load question sets. Please try again.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleStartTest = (questionSetId: number) => {
        sessionStorage.setItem('selectedQuestionSetId', questionSetId.toString());
        navigate(`/test/${questionSetId}`);
    };

    if (!student) return null;

    return (
        <div className="min-h-screen p-6">
            <div className="max-w-4xl mx-auto">
                {/* Header */}
                <div className="bg-white rounded-2xl shadow-lg p-6 mb-6">
                    <h1 className="text-2xl font-bold text-gray-900 mb-2">Select Your Test</h1>
                    <div className="flex items-center gap-4 text-sm text-gray-600">
                        <span className="flex items-center gap-1">
                            <Users className="w-4 h-4" />
                            {student.name}
                        </span>
                        <span>USN: {student.usn}</span>
                        <span>Semester {student.semester}</span>
                        <span className="px-2 py-1 bg-blue-100 text-blue-700 rounded">
                            {student.department}
                        </span>
                    </div>
                </div>

                {/* Question Sets */}
                {loading ? (
                    <div className="flex items-center justify-center py-20">
                        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
                    </div>
                ) : error ? (
                    <div className="bg-red-50 border border-red-200 rounded-lg p-6 text-red-700">
                        {error}
                    </div>
                ) : questionSets.length === 0 ? (
                    <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-6 text-yellow-700">
                        No question sets available for your semester and department.
                    </div>
                ) : (
                    <div className="space-y-4">
                        {questionSets.map((set) => (
                            <div
                                key={set.id}
                                className="bg-white rounded-xl shadow-md hover:shadow-xl transition-all duration-200 p-6"
                            >
                                <div className="flex items-start justify-between">
                                    <div className="flex-1">
                                        <h3 className="text-xl font-semibold text-gray-900 mb-2">
                                            {set.name}
                                        </h3>
                                        <p className="text-gray-600 mb-4">{set.description}</p>
                                        <div className="flex items-center gap-4 text-sm text-gray-500">
                                            <span className="flex items-center gap-1">
                                                <BookOpen className="w-4 h-4" />
                                                {set.questions.length} Questions
                                            </span>
                                            <span className="flex items-center gap-1">
                                                <Clock className="w-4 h-4" />
                                                ~{set.questions.length * 2} minutes
                                            </span>
                                        </div>
                                    </div>
                                    <button
                                        onClick={() => handleStartTest(set.id)}
                                        className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg font-semibold flex items-center gap-2 transition transform hover:scale-105"
                                    >
                                        Start Test
                                        <ArrowRight className="w-4 h-4" />
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default TestSelection;
