import React, { useState, useEffect } from 'react';
import './App.css';
import PasswordHistory from './components/PasswordHistory';

function App() {
    // Estados para o Gerador
    const [generatedPassword, setGeneratedPassword] = useState('Clique no botão para gerar...');

    // Estados para o Validador
    const [passwordToValidate, setPasswordToValidate] = useState('');
    const [validationResult, setValidationResult] = useState(null);

    // Estados para as Políticas
    const [policies, setPolicies] = useState([]);
    const [selectedPolicy, setSelectedPolicy] = useState('');

    // Efeito para buscar as políticas na inicialização
    useEffect(() => {
        fetch('https://localhost:8443/api/politicas')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Erro na API: ${response.statusText}`);
                }
                return response.json();
            })
            .then(data => {
                if (Array.isArray(data)) {
                    setPolicies(data);
                    if (data.length > 0) {
                        setSelectedPolicy(data[0].key);
                    }
                } else {
                    console.error("Erro: A API de políticas não retornou um array.", data);
                    setPolicies([]);
                }
            })
            .catch(error => {
                console.error('Erro ao buscar políticas:', error);
                setPolicies([]);
            });
    }, []);

    const handleGeneratePassword = () => {
        const apiUrl = 'https://localhost:8443/api/gerar?length=16';
        fetch(apiUrl)
            .then(response => response.text())
            .then(data => setGeneratedPassword(data))
            .catch(error => console.error('Erro ao chamar a API de geração:', error));
    };

    const handleValidatePassword = (password, policyKey) => {
        setPasswordToValidate(password);

        if (!password || !policyKey) {
            setValidationResult(null);
            return;
        }

        const apiUrl = `https://localhost:8443/api/validar?policyKey=${policyKey}`;

        fetch(apiUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ password: password })
        })
            .then(response => response.json())
            .then(data => setValidationResult(data))
            .catch(error => console.error('Erro ao chamar a API de validação:', error));
    };

    return (
        <>
            <h1>Gerador e Validador de Senhas Seguro</h1>

            <div className="card">
                <h2>Gerador de Senhas</h2>
                <input type="text" value={generatedPassword} readOnly />
                <button onClick={handleGeneratePassword}>Gerar Nova Senha</button>
            </div>

            <div className="card">
                <h2>Validador de Força e Conformidade</h2>

                <div className="policy-selector">
                    <label htmlFor="policy">Escolha uma política:</label>
                    <select
                        id="policy"
                        value={selectedPolicy}
                        onChange={(e) => {
                            setSelectedPolicy(e.target.value);
                            // Re-valida a senha atual com a nova política
                            handleValidatePassword(passwordToValidate, e.target.value);
                        }}
                    >
                        {policies.map(policy => (
                            <option key={policy.key} value={policy.key}>
                                {policy.name}
                            </option>
                        ))}
                    </select>
                </div>

                <input
                    type="text"
                    placeholder="Digite uma senha para validar"
                    value={passwordToValidate}
                    onChange={(e) => handleValidatePassword(e.target.value, selectedPolicy)}
                />

                {validationResult && passwordToValidate && (
                    <div className="validation-results">
                        <p>
                            <strong>Força:</strong> {validationResult.strength} |
                            <strong> Pontuação:</strong> {validationResult.score} |
                            <strong> Entropia:</strong> {validationResult.entropy.toFixed(2)} bits
                        </p>
                        {validationResult.isPwned && (
                            <p className="pwned-warning"><strong>PERIGO:</strong> Esta senha já foi exposta em um vazamento de dados!</p>
                        )}

                        {validationResult.policyResult && (
                            <div className="policy-check">
                                <h4>Conformidade com a Política: {validationResult.policyResult.isValid ? '✅ Válida' : '❌ Inválida'}</h4>
                                {!validationResult.policyResult.isValid && validationResult.policyResult.brokenRules.length > 0 && (
                                    <ul>
                                        {validationResult.policyResult.brokenRules.map((rule, index) => (
                                            <li key={index} className="broken-rule">{rule}</li>
                                        ))}
                                    </ul>
                                )}
                            </div>
                        )}
                    </div>
                )}
            </div>

            <PasswordHistory />
        </>
    );
}

export default App;