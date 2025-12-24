import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { GraduationCap, User, Hash } from 'lucide-react';
import type { Student } from '../types';

const StudentEntry = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        usn: '',
        name: '',
        semester: '6'
    });
    const [error, setError] = useState('');

    const parseUSN = (usn: string): { department: string; year: string } | null => {
        if (usn.length < 7) return null;
        const year = usn.substring(3, 5);
        const department = usn.substring(5, 7);
        return { department, year };
    };

    const validateUSN = (usn: string): boolean => {
        if (usn.length < 10) return false;
        const parsed = parseUSN(usn);
        if (!parsed) return false;
        // Check if year is numeric and department is alphabetic
        return /^\d{2}$/.test(parsed.year) && /^[A-Z]{2}$/.test(parsed.department);
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (!formData.usn || !formData.name || !formData.semester) {
            setError('All fields are required');
            return;
        }

        const usnUpper = formData.usn.toUpperCase();
        if (!validateUSN(usnUpper)) {
            setError('Invalid USN format. Example: 1MS22CS023');
            return;
        }

        const parsed = parseUSN(usnUpper);
        if (!parsed) {
            setError('Could not parse USN');
            return;
        }

        const student: Student = {
            usn: usnUpper,
            name: formData.name,
            semester: parseInt(formData.semester),
            department: parsed.department,
            yearOfAdmission: parsed.year
        };

        sessionStorage.setItem('student', JSON.stringify(student));
        navigate('/test-selection');
    };

    return (
        <div className="min-h-screen flex items-center justify-center p-4">
            <div className="bg-white rounded-2xl shadow-2xl p-8 w-full max-w-md">
                <div className="text-center mb-8">
                    <div className="inline-flex items-center justify-center w-16 h-16 bg-blue-100 rounded-full mb-4">
                        <GraduationCap className="w-8 h-8 text-blue-600" />
                    </div>
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">
                        Online Examination System
                    </h1>
                    <p className="text-gray-600">Enter your details to begin</p>
                </div>

                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            <div className="flex items-center gap-2">
                                <Hash className="w-4 h-4" />
                                USN
                            </div>
                        </label>
                        <input
                            type="text"
                            value={formData.usn}
                            onChange={(e) => setFormData({ ...formData, usn: e.target.value.toUpperCase() })}
                            placeholder="1MS22CS023"
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            <div className="flex items-center gap-2">
                                <User className="w-4 h-4" />
                                Student Name
                            </div>
                        </label>
                        <input
                            type="text"
                            value={formData.name}
                            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                            placeholder="John Doe"
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            <div className="flex items-center gap-2">
                                <GraduationCap className="w-4 h-4" />
                                Semester
                            </div>
                        </label>
                        <select
                            value={formData.semester}
                            onChange={(e) => setFormData({ ...formData, semester: e.target.value })}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
                        >
                            {[1, 2, 3, 4, 5, 6, 7, 8].map(sem => (
                                <option key={sem} value={sem}>Semester {sem}</option>
                            ))}
                        </select>
                    </div>

                    {error && (
                        <div className="p-4 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
                            {error}
                        </div>
                    )}

                    <button
                        type="submit"
                        className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition duration-200 transform hover:scale-105"
                    >
                        Continue to Test Selection
                    </button>
                </form>

                <div className="mt-6 p-4 bg-blue-50 rounded-lg">
                    <p className="text-xs text-blue-800">
                        <strong>USN Format:</strong> 1MS22CS023<br />
                        • Positions 4-5: Year (22 = 2022)<br />
                        • Positions 6-7: Department (CS, EC, ME, etc.)
                    </p>
                </div>
            </div >
        </div >
    );
};

export default StudentEntry;
