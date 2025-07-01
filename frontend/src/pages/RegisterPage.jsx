import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function RegisterPage() {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const navigate = useNavigate();


    const handleSubmit = async (event) => {
        event.preventDefault();
        setError('');

        try {
            const response = await fetch('https://localhost:8443/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
            });

            if (response.ok) {

                alert('Cadastro realizado com sucesso! Você será redirecionado para a página de login.');
                navigate('/login');
            } else {

                const errorMessage = await response.text();
                setError(errorMessage);
            }
        } catch (err) {

            console.error('Falha na requisição de registro:', err);
            setError('Não foi possível se conectar ao servidor. Tente novamente mais tarde.');
        }
    };


    return (
        <div className="card">
            <h2 style={{ fontSize: '2rem', fontWeight: 'bold', marginBottom: '1.5rem' }}>Cadastro</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="register-email">Email</label>
                    <input
                        type="email"
                        id="register-email"
                        placeholder="seu@email.com"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="register-password">Senha</label>
                    <input
                        type="password"
                        id="register-password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                {error && <p className="error-message">{error}</p>}
                <button type="submit">Cadastrar</button>
            </form>
        </div>
    );
}

export default RegisterPage;