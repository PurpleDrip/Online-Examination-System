import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    BarChart3, Users, BookOpen, TrendingUp, Award,
    Plus, Edit, Trash2, Loader2, LogOut
} from 'lucide-react';
import { getDashboardStats } from '../services/api';
import type { DashboardStats } from '../types';

const AdminDashboard = () => {
    const navigate = useNavigate();
    const [stats, setStats] = useState<DashboardStats | null>(null);
    const [loading, setLoading] = useState(true);
    const [activeTab, setActiveTab] = useState<'overview' | 'questions' | 'sets'>('overview');

    useEffect(() => {
        fetchStats();
    }, []);

    const fetchStats = async () => {
        try {
            setLoading(true);
            const data = await getDashboardStats();
            setStats(data);
        } catch (err) {
            console.error('Failed to fetch stats:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleLogout = () => {
        sessionStorage.removeItem('adminAuth');
        navigate('/admin/login');
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100">
            {/* Header */}
            <div className="bg-white shadow-md border-b border-gray-200">
                <div className="max-w-7xl mx-auto px-6 py-4">
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-2xl font-bold text-gray-900">Admin Dashboard</h1>
                            <p className="text-sm text-gray-600">Examination System Management</p>
                        </div>
                        <button
                            onClick={handleLogout}
                            className="flex items-center gap-2 px-4 py-2 text-red-600 hover:bg-red-50 rounded-lg transition"
                        >
                            <LogOut className="w-4 h-4" />
                            Logout
                        </button>
                    </div>
                </div>
            </div>

            <div className="max-w-7xl mx-auto px-6 py-6">
                {/* Tabs */}
                <div className="bg-white rounded-lg shadow-md mb-6">
                    <div className="flex border-b border-gray-200">
                        <button
                            onClick={() => setActiveTab('overview')}
                            className={`px-6 py-4 font-semibold transition ${activeTab === 'overview'
                                    ? 'text-blue-600 border-b-2 border-blue-600'
                                    : 'text-gray-600 hover:text-gray-900'
                                }`}
                        >
                            <div className="flex items-center gap-2">
                                <BarChart3 className="w-5 h-5" />
                                Overview
                            </div>
                        </button>
                        <button
                            onClick={() => setActiveTab('questions')}
                            className={`px-6 py-4 font-semibold transition ${activeTab === 'questions'
                                    ? 'text-blue-600 border-b-2 border-blue-600'
                                    : 'text-gray-600 hover:text-gray-900'
                                }`}
                        >
                            <div className="flex items-center gap-2">
                                <BookOpen className="w-5 h-5" />
                                Questions
                            </div>
                        </button>
                        <button
                            onClick={() => setActiveTab('sets')}
                            className={`px-6 py-4 font-semibold transition ${activeTab === 'sets'
                                    ? 'text-blue-600 border-b-2 border-blue-600'
                                    : 'text-gray-600 hover:text-gray-900'
                                }`}
                        >
                            <div className="flex items-center gap-2">
                                <Users className="w-5 h-5" />
                                Question Sets
                            </div>
                        </button>
                    </div>
                </div>

                {/* Overview Tab */}
                {activeTab === 'overview' && stats && (
                    <div>
                        {/* Statistics Cards */}
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
                            <div className="bg-white rounded-xl shadow-md p-6 border-l-4 border-blue-500">
                                <div className="flex items-center justify-between mb-4">
                                    <div className="p-3 bg-blue-100 rounded-lg">
                                        <Users className="w-6 h-6 text-blue-600" />
                                    </div>
                                </div>
                                <h3 className="text-gray-600 text-sm mb-1">Total Tests Taken</h3>
                                <p className="text-3xl font-bold text-gray-900">{stats.totalTests}</p>
                            </div>

                            <div className="bg-white rounded-xl shadow-md p-6 border-l-4 border-green-500">
                                <div className="flex items-center justify-between mb-4">
                                    <div className="p-3 bg-green-100 rounded-lg">
                                        <TrendingUp className="w-6 h-6 text-green-600" />
                                    </div>
                                </div>
                                <h3 className="text-gray-600 text-sm mb-1">Average Score</h3>
                                <p className="text-3xl font-bold text-gray-900">{stats.averageScore.toFixed(1)}</p>
                            </div>

                            <div className="bg-white rounded-xl shadow-md p-6 border-l-4 border-purple-500">
                                <div className="flex items-center justify-between mb-4">
                                    <div className="p-3 bg-purple-100 rounded-lg">
                                        <BarChart3 className="w-6 h-6 text-purple-600" />
                                    </div>
                                </div>
                                <h3 className="text-gray-600 text-sm mb-1">Average Percentage</h3>
                                <p className="text-3xl font-bold text-gray-900">{stats.averagePercentage.toFixed(1)}%</p>
                            </div>

                            <div className="bg-white rounded-xl shadow-md p-6 border-l-4 border-yellow-500">
                                <div className="flex items-center justify-between mb-4">
                                    <div className="p-3 bg-yellow-100 rounded-lg">
                                        <Award className="w-6 h-6 text-yellow-600" />
                                    </div>
                                </div>
                                <h3 className="text-gray-600 text-sm mb-1">Highest Score</h3>
                                <p className="text-3xl font-bold text-gray-900">{stats.highestScore}</p>
                            </div>
                        </div>

                        {/* Performance Insights */}
                        <div className="bg-white rounded-xl shadow-md p-6">
                            <h2 className="text-xl font-bold text-gray-900 mb-4">Performance Insights</h2>
                            <div className="space-y-4">
                                <div className="flex items-center justify-between p-4 bg-blue-50 rounded-lg">
                                    <div>
                                        <p className="font-semibold text-gray-900">System Health</p>
                                        <p className="text-sm text-gray-600">All services operational</p>
                                    </div>
                                    <div className="w-3 h-3 bg-green-500 rounded-full animate-pulse"></div>
                                </div>
                                <div className="flex items-center justify-between p-4 bg-green-50 rounded-lg">
                                    <div>
                                        <p className="font-semibold text-gray-900">Pass Rate</p>
                                        <p className="text-sm text-gray-600">
                                            {stats.averagePercentage >= 60 ? 'Above' : 'Below'} average
                                        </p>
                                    </div>
                                    <p className="text-2xl font-bold text-green-600">
                                        {stats.averagePercentage.toFixed(0)}%
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                {/* Questions Tab */}
                {activeTab === 'questions' && (
                    <div className="bg-white rounded-xl shadow-md p-6">
                        <div className="flex items-center justify-between mb-6">
                            <h2 className="text-xl font-bold text-gray-900">Manage Questions</h2>
                            <button
                                onClick={() => navigate('/admin/questions/new')}
                                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-semibold flex items-center gap-2 transition"
                            >
                                <Plus className="w-5 h-5" />
                                Add Question
                            </button>
                        </div>
                        <p className="text-gray-600 text-center py-8">
                            Click "Add Question" to create new questions or navigate to question management.
                        </p>
                    </div>
                )}

                {/* Question Sets Tab */}
                {activeTab === 'sets' && (
                    <div className="bg-white rounded-xl shadow-md p-6">
                        <div className="flex items-center justify-between mb-6">
                            <h2 className="text-xl font-bold text-gray-900">Manage Question Sets</h2>
                            <button
                                onClick={() => navigate('/admin/question-sets/new')}
                                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-semibold flex items-center gap-2 transition"
                            >
                                <Plus className="w-5 h-5" />
                                Create Set
                            </button>
                        </div>
                        <p className="text-gray-600 text-center py-8">
                            Click "Create Set" to build new question sets or navigate to set management.
                        </p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default AdminDashboard;
