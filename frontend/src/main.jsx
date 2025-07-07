import React from 'react';
import ReactDOM from 'react-dom/client';
import {
    createBrowserRouter,
    RouterProvider,
    Link,
    Outlet
} from 'react-router-dom';

import ProtectedRoute from './router/ProtectedRoute';

import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import { AuthProvider, useAuth } from './context/AuthContext';

import './index.css';


const AppLayout = () => {
    const { isAuthenticated, user, logout } = useAuth();

    const handleLogout = () => {

        logout();

    };

    return (
        <>
            <header>
                <nav>
                    <div className="nav-main-links">
                        {isAuthenticated ? (
                            <Link to="/">Gerador/Validador</Link>
                        ) : (
                            <span style={{ fontWeight: 'bold', color: 'white', cursor: 'default' }}></span>
                        )}
                    </div>

                    <div className="auth-links">
                        {isAuthenticated ? (
                            <>
                                <span>Olá, {user?.sub}</span>
                                <button onClick={handleLogout} className="btn-primary">Logout</button>
                            </>
                        ) : (
                            <>
                                <Link to="/login">Login</Link>
                                <Link to="/register">Cadastro</Link>
                            </>
                        )}
                    </div>
                </nav>
            </header>
            <main>
                <Outlet />
            </main>
        </>
    );
};


const router = createBrowserRouter([
    {
        element: <AppLayout />,
        children: [
            {
                path: '/',
                element: <ProtectedRoute />,

                children: [
                    {
                        index: true,
                        element: <HomePage />,
                    },
                ],
            },

            {
                path: '/login',
                element: <LoginPage />,
            },
            {
                path: '/register',
                element: <RegisterPage />,
            },
        ],
    },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <AuthProvider>
            <RouterProvider router={router} />
        </AuthProvider>
    </React.StrictMode>
);