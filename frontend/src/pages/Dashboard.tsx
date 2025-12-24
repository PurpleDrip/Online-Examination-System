import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { BarChart3, Users, TrendingUp, Award, Home, Loader2 } from 'lucide-react';
import { getDashboardStats, getResultsByUsn } from '../services/api';
import type { DashboardStats, Result } from '../types';

const Dashboard = () => {
    const navigate = useNavigate();
    const [stats, setStats] = useState<DashboardStats | null>(null);
    const [recentResults, setRecentResults] = useState<Result[]>([]);
    const [loading, setLoading] = useState(true);
    const [usn, setUsn] = useState('');

    useEffect(() => {
        // Try to get USN from session storage
        const studentData = sessionStorage.getItem('student');
        if (studentData) {
            const student = JSON.parse(studentData);
            setUsn(student.usn);
            fetchUserResults(student.usn);
        }
        fetchOverallStats();
    }, []);

    const fetchOverallStats = async () => {
        try {
            const data = await getDashboardStats();
            setStats(data);
        } catch (err) {
            console.error('Failed to fetch dashboard stats:', err);
        }
    };

    const fetchUserResults = async (studentUsn: string) => {
        try {
            setLoading(true);
            const results = await getResultsByUsn(studentUsn);
            setRecentResults(results);
        } catch (err) {
            console.error('Failed to fetch user results:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleNewTest = () => {
        navigate('/');
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
            </div>
        );
    }

    return (
        <div className="min-h-screen p-6 bg-gradient-to-br from-blue-50 to-indigo-100">
            <div className="max-w-7xl mx-auto">
                {/* Header */}
                <div className="bg-white rounded-2xl shadow-lg p-6 mb-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">Dashboard</h1>
                            {usn && (
                                <p className="text-gray-600">Welcome back, {usn}</p>
                            )}
                        </div>
                        <button
                            onClick={handleNewTest}
                            className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg font-semibold flex items-center gap-2 transition"
                        >
                            <Home className="w-5 h-5" />
                            Take New Test
                        </button>
                    </div>
                </div>

                {/* Overall Statistics */}
                {stats && (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
                        <div className="bg-white rounded-xl shadow-md p-6">
                            <div className="flex items-center justify-between mb-4">
                                <div className="p-3 bg-blue-100 rounded-lg">
                                    <Users className="w-6 h-6 text-blue-600" />
                                </div>
                            </div>
                            <h3 className="text-gray-600 text-sm mb-1">Total Tests</h3>
                            <p className="text-3xl font-bold text-gray-900">{stats.totalTests}</p>
                        </div>

                        <div className="bg-white rounded-xl shadow-md p-6">
                            <div className="flex items-center justify-between mb-4">
                                <div className="p-3 bg-green-100 rounded-lg">
                                    <TrendingUp className="w-6 h-6 text-green-600" />
                                </div>
                            </div>
                            <h3 className="text-gray-600 text-sm mb-1">Average Score</h3>
                            <p className="text-3xl font-bold text-gray-900">{stats.averageScore.toFixed(1)}</p>
                        </div>

                        <div className="bg-white rounded-xl shadow-md p-6">
                            <div className="flex items-center justify-between mb-4">
                                <div className="p-3 bg-purple-100 rounded-lg">
                                    <BarChart3 className="w-6 h-6 text-purple-600" />
                                </div>
                            </div>
                            <h3 className="text-gray-600 text-sm mb-1">Average %</h3>
                            <p className="text-3xl font-bold text-gray-900">{stats.averagePercentage.toFixed(1)}%</p>
                        </div>

                        <div className="bg-white rounded-xl shadow-md p-6">
                            <div className="flex items-center justify-between mb-4">
                                <div className="p-3 bg-yellow-100 rounded-lg">
                                    <Award className="w-6 h-6 text-yellow-600" />
                                </div>
                            </div>
                            <h3 className="text-gray-600 text-sm mb-1">Highest Score</h3>
                            <p className="text-3xl font-bold text-gray-900">{stats.highestScore}</p>
                        </div>
                    </div>
                )}

                {/* Recent Test Results */}
                <div className="bg-white rounded-2xl shadow-lg p-6">
                    <h2 className="text-2xl font-bold text-gray-900 mb-6">
                        {usn ? 'Your Test History' : 'Recent Tests'}
                    </h2>

                    {recentResults.length === 0 ? (
                        <div className="text-center py-12">
                            <p className="text-gray-600 mb-4">No test results yet</p>
                            <button
                                onClick={handleNewTest}
                                className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg font-semibold transition"
                            >
                                Take Your First Test
                            </button>
                        </div>
                    ) : (
                        <div className="overflow-x-auto">
                            <table className="w-full">
                                <thead>
                                    <tr className="border-b-2 border-gray-200">
                                        <th className="text-left py-3 px-4 font-semibold text-gray-700">Date</th>
                                        <th className="text-left py-3 px-4 font-semibold text-gray-700">Student</th>
                                        <th className="text-left py-3 px-4 font-semibold text-gray-700">Semester</th>
                                        <th className="text-left py-3 px-4 font-semibold text-gray-700">Department</th>
                                        <th className="text-center py-3 px-4 font-semibold text-gray-700">Score</th>
                                        <th className="text-center py-3 px-4 font-semibold text-gray-700">Percentage</th>
                                        <th className="text-center py-3 px-4 font-semibold text-gray-700">Grade</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {recentResults.map((result) => {
                                        const getGrade = (percentage: number) => {
                                            if (percentage >= 90) return { grade: 'A+', color: 'text-green-600 bg-green-50' };
                                            if (percentage >= 80) return { grade: 'A', color: 'text-green-500 bg-green-50' };
                                            if (percentage >= 70) return { grade: 'B', color: 'text-blue-600 bg-blue-50' };
                                            if (percentage >= 60) return { grade: 'C', color: 'text-yellow-600 bg-yellow-50' };
                                            if (percentage >= 50) return { grade: 'D', color: 'text-orange-600 bg-orange-50' };
                                            return { grade: 'F', color: 'text-red-600 bg-red-50' };
                                        };

                                        const gradeInfo = getGrade(result.percentage);

                                        return (
                                            <tr key={result.id} className="border-b border-gray-100 hover:bg-gray-50">
                                                <td className="py-4 px-4 text-gray-600">
                                                    {new Date(result.createdAt).toLocaleDateString()}
                                                </td>
                                                <td className="py-4 px-4 text-gray-900 font-medium">
                                                    {result.studentName}
                                                </td>
                                                <td className="py-4 px-4 text-gray-600">{result.semester}</td>
                                                <td className="py-4 px-4">
                                                    <span className="px-2 py-1 bg-blue-100 text-blue-700 rounded text-sm">
                                                        {result.department}
                                                    </span>
                                                </td>
                                                <td className="py-4 px-4 text-center font-semibold text-gray-900">
                                                    {result.correctAnswers}/{result.totalQuestions}
                                                </td>
                                                <td className="py-4 px-4 text-center font-semibold text-gray-900">
                                                    {result.percentage.toFixed(1)}%
                                                </td>
                                                <td className="py-4 px-4 text-center">
                                                    <span className={`px-3 py-1 rounded-full font-bold ${gradeInfo.color}`}>
                                                        {gradeInfo.grade}
                                                    </span>
                                                </td>
                                            </tr>
                                        );
                                    })}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Dashboard;
