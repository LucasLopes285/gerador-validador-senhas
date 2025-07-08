import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useAuthFetch } from '../hooks/useAuthFetch';

/**
 * Componente que gerencia a exibição do histórico de senhas.
 * Inclui verificação de segunda senha e um formulário para cadastrá-la.
 */
function PasswordHistory() {
    // --- Estados para o fluxo principal de visualização ---
    const [history, setHistory] = useState([]);
    const [secondPassword, setSecondPassword] = useState('');
    const [viewError, setViewError] = useState('');
    const [isHistoryVisible, setIsHistoryVisible] = useState(false);

    // --- Estados para o fluxo de cadastro da segunda senha ---
    const [showSetPasswordForm, setShowSetPasswordForm] = useState(false);
    const [newSecondPassword, setNewSecondPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [setError, setSetError] = useState('');

    // --- Hooks globais ---
    const { isAuthenticated } = useAuth();
    const authFetch = useAuthFetch();

    // Função para buscar o histórico no backend
    const handleFetchHistory = async (event) => {
        event.preventDefault();
        setViewError('');

        try {
            const response = await authFetch('/api/historico/view', {
                method: 'POST',
                body: JSON.stringify({ secondPassword: secondPassword })
            });

            if (response.ok) {
                const data = await response.json();
                setHistory(data);
                setIsHistoryVisible(true); // Exibe a tabela com o histórico
            } else {
                const errorMessage = await response.text();
                setViewError(errorMessage);
                setIsHistoryVisible(false);
            }
        } catch (err) {
            console.error(err);
            setViewError('Falha ao se comunicar com o servidor.');
            setIsHistoryVisible(false);
        }
    };

    // Função para cadastrar a nova senha de segurança
    const handleSetSecondPassword = async (event) => {
        event.preventDefault();
        setSetError('');

        if (newSecondPassword !== confirmPassword) {
            setSetError('As senhas não coincidem.');
            return;
        }

        try {
            const response = await authFetch('/api/auth/set-second-factor', {
                method: 'POST',
                body: JSON.stringify({ password: newSecondPassword })
            });

            if (response.ok) {
                alert('Senha de segurança cadastrada com sucesso!');
                setShowSetPasswordForm(false);
                setNewSecondPassword('');
                setConfirmPassword('');
            } else {
                const errorMessage = await response.text();
                setSetError(errorMessage);
            }
        } catch (err) {
            console.error(err);
            setSetError('Falha ao se comunicar com o servidor.');
        }
    };


    const formatDateTime = (dateTimeString) => {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' };
        return new Date(dateTimeString).toLocaleString('pt-BR', options);
    };


    if (!isAuthenticated) {
        return null;
    }


    if (isHistoryVisible) {
        return (
            <div className="card">
                <h2>Histórico de Senhas Geradas</h2>
                <button onClick={() => { setIsHistoryVisible(false); setSecondPassword(''); setViewError(''); }} style={{width: 'auto', marginBottom: '1rem', backgroundColor: '#4b5563'}}>Ocultar Histórico</button>
                {history.length > 0 ? (
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Senha (Descriptografada)</th>
                            <th>Data de Criação</th>
                        </tr>
                        </thead>
                        <tbody>
                        {history.map(record => (
                            <tr key={record.id}>
                                <td>{record.id}</td>
                                <td>{record.passwordValue}</td>
                                <td>{formatDateTime(record.createdAt)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : <p>Nenhum histórico para exibir.</p>}
            </div>
        );
    }


    return (
        <div className="card">
            {showSetPasswordForm ? (
                // --- Formulário de CADASTRO da Segunda Senha ---
                <div className="secondary-password-card">
                    <h2 style={{marginBottom: '1rem'}}>Cadastrar Senha de Segurança</h2>
                    <form onSubmit={handleSetSecondPassword}>
                        <div className="form-group">
                            <label htmlFor="new-second-password">Nova Senha de Segurança</label>
                            <input type="password" id="new-second-password" value={newSecondPassword} onChange={(e) => setNewSecondPassword(e.target.value)} required />
                        </div>
                        <div className="form-group">
                            <label htmlFor="confirm-password">Confirmar Senha</label>
                            <input type="password" id="confirm-password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} required />
                        </div>
                        {setError && <p className="error-message">{setError}</p>}
                        <button type="submit" className="btn-primary">Cadastrar Senha</button>
                        <button type="button" onClick={() => setShowSetPasswordForm(false)} style={{marginTop: '0.5rem', backgroundColor: '#4b5563'}}>Cancelar</button>
                    </form>
                </div>
            ) : (
                // --- Formulário para ACESSAR o Histórico ---
                <div className="form-container">
                    <h2>Acessar Histórico de Senhas</h2>
                    <p style={{color: '#9ca3af', fontSize: '0.9rem'}}>Por segurança, digite sua segunda senha para ver o histórico.</p>
                    <form onSubmit={handleFetchHistory} style={{ marginTop: '1rem' }}>
                        <div className="form-group">
                            <label htmlFor="second-password">Senha de Segurança</label>
                            <input type="password" id="second-password" value={secondPassword} onChange={(e) => setSecondPassword(e.target.value)} required />
                        </div>
                        {viewError && <p className="error-message">{viewError}</p>}
                        <button type="submit" className="btn-primary">Ver Histórico</button>
                    </form>
                    <p style={{marginTop: '1.5rem', fontSize: '0.9rem'}}>
                        Não possui uma senha?{' '}
                        <span onClick={() => setShowSetPasswordForm(true)} style={{color: '#60a5fa', cursor: 'pointer', textDecoration: 'underline'}}>
                        Crie uma nova.
                        </span>
                    </p>
                </div>
            )}
        </div>
    );
}

export default PasswordHistory;