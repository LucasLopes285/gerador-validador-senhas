import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

export const useAuthFetch = () => {
    const { authToken, logout } = useAuth();
    const navigate = useNavigate();

    const authFetch = async (url, options = {}) => {

        if (!authToken) {
            logout();
            navigate('/login');
            return Promise.reject(new Error('Usuário não autenticado.'));
        }

        const response = await fetch(url, {
            ...options,
            headers: {
                ...options.headers,
                ...(options.body && {'Content-Type': 'application/json'}),
                'Authorization': `Bearer ${authToken}`,
            },
        });

        if (response.status === 401 || response.status === 403) {
            logout();
            navigate('/login');
            throw new Error('Sessão expirada ou inválida. Por favor, faça o login novamente.');
        }

        return response;
    };

    return authFetch;
};