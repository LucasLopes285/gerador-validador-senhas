import { useState } from 'react';
import './App.css';

function App() {
    const [generatedPassword, setGeneratedPassword] = useState('Clique no botão para gerar...');

    // -- ESTADOS PARA O VALIDADOR --
    const [passwordToValidate, setPasswordToValidate] = useState('');
    const [validationResult, setValidationResult] = useState(null);

    const handleGeneratePassword = () => {
        const apiUrl = 'https://localhost:8443/api/gerar?length=16';
        fetch(apiUrl)
            .then(response => response.text())
            .then(data => {
                setGeneratedPassword(data);
            })
            .catch(error => {
                console.error('Erro ao chamar a API de geração:', error);
                setGeneratedPassword('Erro ao gerar a senha.');
            });
    };

    // --FUNÇÃO PARA VALIDAR A SENHA --
    const handleValidatePassword = (password) => {
        setPasswordToValidate(password);

        if (password.length === 0) {
            setValidationResult(null);
            return;
        }

        const apiUrl = 'https://localhost:8443/api/validar';

        // Fazendo uma requisição POST
        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ password: password })
        })
            .then(response => response.json())
            .then(data => {
                setValidationResult(data);
            })
            .catch(error => {
                console.error('Erro ao chamar a API de validação:', error);
                setValidationResult(null);
            });
    };

    return (
        <>
            {/* Seção do Gerador */}
            <div className="card">
                <h2>Gerador de Senhas</h2>
                <input type="text" value={generatedPassword} readOnly />
                <button onClick={handleGeneratePassword}>
                    Gerar Nova Senha
                </button>
            </div>

            {/* -- SEÇÃO DO VALIDADOR -- */}
            <div className="card">
                <h2>Validador de Força</h2>
                <input
                    type="text"
                    placeholder="Digite uma senha para validar"
                    value={passwordToValidate}
                    onChange={(e) => handleValidatePassword(e.target.value)}
                />
                {/* Renderização condicional: só mostra os resultados se eles existirem */}
                {validationResult && (
                    <div className="validation-results">
                        <p><strong>Força:</strong> {validationResult.strength} | <strong>Pontuação:</strong> {validationResult.score} | <strong>Entropia:</strong> {validationResult.entropy.toFixed(2)} bits</p>
                        <ul>
                            {validationResult.suggestions.map((suggestion, index) => (
                                <li key={index}>{suggestion}</li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
        </>
    );
}

export default App;