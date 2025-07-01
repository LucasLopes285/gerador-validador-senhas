import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';

/**
 * Componente final para exibir o histórico de senhas.
 * Ele busca o histórico apenas se o usuário estiver autenticado.
 */
function PasswordHistory() {

    const [history, setHistory] = useState([]);


    const { authToken, isAuthenticated } = useAuth();


    useEffect(() => {

        if (isAuthenticated) {
            const apiUrl = '/api/historico';

            fetch(apiUrl, {
                method: 'GET',

                headers: {

                    'Authorization': `Bearer ${authToken}`
                }
            })
                .then(response => {

                    if (!response.ok) {
                        throw new Error('Falha na rede ou na resposta da API');
                    }
                    return response.json();
                })
                .then(data => {
                    setHistory(data);
                })
                .catch(error => {
                    console.error('Erro ao buscar o histórico:', error);
                    setHistory([]);
                });
        } else {

            setHistory([]);
        }
    }, [isAuthenticated, authToken]);

    // Função para formatar a data para o padrão brasileiro
    const formatDateTime = (dateTimeString) => {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' };
        return new Date(dateTimeString).toLocaleString('pt-BR', options);
    };



    if (!isAuthenticated) {
        return (
            <div className="card">
                <h2>Histórico de Senhas Geradas</h2>
                <p>Por favor, faça login para ver seu histórico.</p>
            </div>
        );
    }


    return (
        <div className="card">
            <h2>Histórico de Senhas Geradas</h2>
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
            ) : (
                <p>Nenhum histórico encontrado. Gere uma nova senha para começar.</p>
            )}
        </div>
    );
}

export default PasswordHistory;