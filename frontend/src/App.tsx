import { BrowserRouter, Routes, Route } from 'react-router-dom';
import StudentEntry from './pages/StudentEntry';
import TestSelection from './pages/TestSelection';
import TestInterface from './pages/TestInterface';
import ResultsDisplay from './pages/ResultsDisplay';
import Dashboard from './pages/Dashboard';
import AdminLogin from './pages/AdminLogin';
import AdminDashboard from './pages/AdminDashboard';
import QuestionManagement from './pages/QuestionManagement';
import QuestionSetManagement from './pages/QuestionSetManagement';

function App() {
    return (
        <BrowserRouter>
            <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
                <Routes>
                    {/* Student Routes */}
                    <Route path="/" element={<StudentEntry />} />
                    <Route path="/test-selection" element={<TestSelection />} />
                    <Route path="/test/:sessionId" element={<TestInterface />} />
                    <Route path="/results/:sessionId" element={<ResultsDisplay />} />
                    <Route path="/dashboard" element={<Dashboard />} />

                    {/* Admin Routes */}
                    <Route path="/admin/login" element={<AdminLogin />} />
                    <Route path="/admin/dashboard" element={<AdminDashboard />} />
                    <Route path="/admin/questions/new" element={<QuestionManagement />} />
                    <Route path="/admin/question-sets/new" element={<QuestionSetManagement />} />
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;
