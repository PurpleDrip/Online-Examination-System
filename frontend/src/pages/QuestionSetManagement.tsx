import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, ArrowLeft, Loader2, Save, X } from 'lucide-react';
import axios from 'axios';
import type { QuestionSet, Question } from '../types';

const QuestionSetManagement = () => {
    const navigate = useNavigate();
    const [questionSets, setQuestionSets] = useState<QuestionSet[]>([]);
    const [allQuestions, setAllQuestions] = useState<Question[]>([]);
    const [loading, setLoading] = useState(true);
    const [showForm, setShowForm] = useState(false);
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        semester: 6,
        department: 'CS',
        questionIds: [] as number[]
    });
    const [selectedQuestions, setSelectedQuestions] = useState<number[]>([]);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            setLoading(true);
            const [setsRes, questionsRes] = await Promise.all([
                axios.get('http://localhost:8080/api/question-sets'),
                axios.get('http://localhost:8080/api/questions')
            ]);
            setQuestionSets(setsRes.data);
            setAllQuestions(questionsRes.data);
        } catch (err) {
            console.error('Failed to fetch data:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            // Create the question set first
            const setResponse = await axios.post('http://localhost:8080/api/question-sets', {
                name: formData.name,
                description: formData.description,
                semester: formData.semester,
                department: formData.department,
                questions: []
            });

            const setId = setResponse.data.id;

            // Add selected questions to the set
            for (const questionId of selectedQuestions) {
                await axios.post(`http://localhost:8080/api/question-sets/${setId}/questions/${questionId}`);
            }

            setShowForm(false);
            resetForm();
            fetchData();
        } catch (err) {
            console.error('Failed to create question set:', err);
            alert('Failed to create question set');
        }
    };

    const handleQuestionToggle = (questionId: number) => {
        setSelectedQuestions(prev =>
            prev.includes(questionId)
                ? prev.filter(id => id !== questionId)
                : [...prev, questionId]
        );
    };

    const resetForm = () => {
        setFormData({
            name: '',
            description: '',
            semester: 6,
            department: 'CS',
            questionIds: []
        });
        setSelectedQuestions([]);
    };

    const getFilteredQuestions = () => {
        return allQuestions.filter(
            q => q.semester === formData.semester && q.department === formData.department
        );
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 p-6">
            <div className="max-w-6xl mx-auto">
                {/* Header */}
                <div className="bg-white rounded-xl shadow-md p-6 mb-6">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center gap-4">
                            <button
                                onClick={() => navigate('/admin/dashboard')}
                                className="p-2 hover:bg-gray-100 rounded-lg transition"
                            >
                                <ArrowLeft className="w-5 h-5" />
                            </button>
                            <div>
                                <h1 className="text-2xl font-bold text-gray-900">Question Set Management</h1>
                                <p className="text-sm text-gray-600">{questionSets.length} sets total</p>
                            </div>
                        </div>
                        {!showForm && (
                            <button
                                onClick={() => setShowForm(true)}
                                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-semibold flex items-center gap-2 transition"
                            >
                                <Plus className="w-5 h-5" />
                                Create Set
                            </button>
                        )}
                    </div>
                </div>

                {/* Form */}
                {showForm && (
                    <div className="bg-white rounded-xl shadow-md p-6 mb-6">
                        <h2 className="text-xl font-bold text-gray-900 mb-4">New Question Set</h2>
                        <form onSubmit={handleSubmit} className="space-y-6">
                            <div className="grid grid-cols-2 gap-4">
                                <div className="col-span-2">
                                    <label className="block text-sm font-medium text-gray-700 mb-2">Set Name</label>
                                    <input
                                        type="text"
                                        value={formData.name}
                                        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                                        required
                                        placeholder="e.g., Data Structures & Algorithms - Set 1"
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                    />
                                </div>
                                <div className="col-span-2">
                                    <label className="block text-sm font-medium text-gray-700 mb-2">Description</label>
                                    <textarea
                                        value={formData.description}
                                        onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                                        required
                                        rows={2}
                                        placeholder="Brief description of the question set"
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">Semester</label>
                                    <select
                                        value={formData.semester}
                                        onChange={(e) => setFormData({ ...formData, semester: parseInt(e.target.value) })}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                    >
                                        {[1, 2, 3, 4, 5, 6, 7, 8].map(sem => (
                                            <option key={sem} value={sem}>Semester {sem}</option>
                                        ))}
                                    </select>
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">Department</label>
                                    <input
                                        type="text"
                                        value={formData.department}
                                        onChange={(e) => setFormData({ ...formData, department: e.target.value.toUpperCase() })}
                                        required
                                        maxLength={10}
                                        placeholder="CS, EC, ME..."
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                    />
                                </div>
                            </div>

                            {/* Question Selection */}
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Select Questions ({selectedQuestions.length} selected)
                                </label>
                                <div className="border border-gray-300 rounded-lg p-4 max-h-96 overflow-y-auto">
                                    {getFilteredQuestions().length === 0 ? (
                                        <p className="text-gray-500 text-center py-4">
                                            No questions available for Semester {formData.semester} - {formData.department}
                                        </p>
                                    ) : (
                                        <div className="space-y-2">
                                            {getFilteredQuestions().map((question) => (
                                                <div
                                                    key={question.id}
                                                    className={`p-3 rounded-lg border-2 cursor-pointer transition ${selectedQuestions.includes(question.id)
                                                            ? 'border-blue-500 bg-blue-50'
                                                            : 'border-gray-200 hover:border-blue-300'
                                                        }`}
                                                    onClick={() => handleQuestionToggle(question.id)}
                                                >
                                                    <div className="flex items-start gap-3">
                                                        <input
                                                            type="checkbox"
                                                            checked={selectedQuestions.includes(question.id)}
                                                            onChange={() => { }}
                                                            className="mt-1"
                                                        />
                                                        <div className="flex-1">
                                                            <p className="text-gray-900 font-medium">{question.questionText}</p>
                                                            <p className="text-sm text-gray-600 mt-1">
                                                                Correct Answer: <span className="font-semibold text-green-600">{question.correctOption}</span>
                                                            </p>
                                                        </div>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            </div>

                            <div className="flex gap-4">
                                <button
                                    type="submit"
                                    disabled={selectedQuestions.length === 0}
                                    className="bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 text-white px-6 py-2 rounded-lg font-semibold flex items-center gap-2 transition"
                                >
                                    <Save className="w-4 h-4" />
                                    Create Set
                                </button>
                                <button
                                    type="button"
                                    onClick={() => {
                                        setShowForm(false);
                                        resetForm();
                                    }}
                                    className="bg-gray-200 hover:bg-gray-300 text-gray-700 px-6 py-2 rounded-lg font-semibold transition"
                                >
                                    Cancel
                                </button>
                            </div>
                        </form>
                    </div>
                )}

                {/* Question Sets List */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {questionSets.map((set) => (
                        <div key={set.id} className="bg-white rounded-xl shadow-md p-6">
                            <div className="flex items-start justify-between mb-4">
                                <div className="flex-1">
                                    <h3 className="text-lg font-bold text-gray-900 mb-2">{set.name}</h3>
                                    <p className="text-sm text-gray-600 mb-3">{set.description}</p>
                                    <div className="flex items-center gap-4 text-sm">
                                        <span className="px-2 py-1 bg-blue-100 text-blue-700 rounded">
                                            Sem {set.semester}
                                        </span>
                                        <span className="px-2 py-1 bg-purple-100 text-purple-700 rounded">
                                            {set.department}
                                        </span>
                                        <span className="text-gray-600">
                                            {set.questions.length} questions
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>

                {questionSets.length === 0 && !showForm && (
                    <div className="bg-white rounded-xl shadow-md p-12 text-center">
                        <p className="text-gray-600 mb-4">No question sets created yet</p>
                        <button
                            onClick={() => setShowForm(true)}
                            className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg font-semibold transition"
                        >
                            Create Your First Set
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default QuestionSetManagement;
