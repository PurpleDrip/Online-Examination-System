# Online Examination System - Frontend

Modern React-based frontend for the Online Examination System.

## Features

- Student entry with USN validation
- Test selection based on semester and department
- Interactive test interface
- Real-time results display
- Responsive design

## Tech Stack

- React 18 + TypeScript
- Vite
- TailwindCSS
- React Router
- Axios

## Getting Started

```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build
```

## Environment

The frontend connects to the API Gateway at `http://localhost:8080`

## Project Structure

```
src/
├── components/       # React components
├── services/        # API integration
├── types/           # TypeScript interfaces
├── lib/             # Utilities
└── pages/           # Page components
```

## Usage

1. Enter USN, Name, and Semester
2. Select an eligible question set
3. Answer questions
4. Submit test
5. View results

## Development

- Dev server runs on http://localhost:5173
- Hot reload enabled
- TypeScript strict mode
