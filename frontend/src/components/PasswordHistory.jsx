import React, {useState, useEffect} from 'react';

function PasswordHistory(){

    const [history, setHistory] = useState([]);

    useEffect(() => {
        const apiUrl = 'https://localhost:8443/api/historico';

        fetch((apiUrl))
            .then(response => {
                if (!response.ok) {
                    throw new Error('Falha na rede ou na resposta da API');
                }
                return response.json();
            })
            .then(data => {
                setHistory(data); // Armazena a lista recebida no nosso estado
            })
            .catch(error => {
                console.error('Erro ao buscar o histórico:', error);
            });

    }, [])

    const formatDateTime = (dateTimeString) => {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' };
        return new Date(dateTimeString).toLocaleString('pt-BR', options);

    };

    return(
        <div className="card">
            <h2>Histórico de Senhas Geradas</h2>
            {history.length > 0 ? (
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Senha</th>
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