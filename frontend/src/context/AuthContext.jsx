import React, { createContext, useState, useContext, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [authToken, setAuthToken] = useState(null);
    const [user, setUser] = useState(null);

    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        try {
            const token = localStorage.getItem('authToken');
            if (token) {
                const decodedUser = jwtDecode(token);
                setAuthToken(token);
                setUser(decodedUser);
            }
        } catch (error) {
            console.error("Erro ao processar o token inicial:", error);

            localStorage.removeItem('authToken');
        } finally {

            setIsLoading(false);
        }
    }, []);

    const login = (token) => {
        const decodedUser = jwtDecode(token);
        localStorage.setItem('authToken', token);
        setAuthToken(token);
        setUser(decodedUser);
    };

    const logout = () => {
        localStorage.removeItem('authToken');
        setAuthToken(null);
        setUser(null);
    };

    const value = {
        authToken,
        user,
        isLoading,
        isAuthenticated: !!authToken,
        login,
        logout,
    };


    return (
        <AuthContext.Provider value={value}>
            {!isLoading && children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
};