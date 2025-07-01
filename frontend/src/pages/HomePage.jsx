import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import PasswordHistory from '../components/PasswordHistory';
import '../App.css';

function HomePage() {

    const { isAuthenticated, authToken } = useAuth();


    const [generatedPassword, setGeneratedPassword] = useState(
        isAuthenticated ? 'Clique no botão para gerar...' : 'Faça login para usar as funcionalidades'
    );
    const [passwordToValidate, setPasswordToValidate] = useState('');
    const [validationResult, setValidationResult] = useState(null);
    const [policies, setPolicies] = useState([]);
    const [selectedPolicy, setSelectedPolicy] = useState('');


    useEffect(() => {
        if (isAuthenticated) {
            fetch('/api/politicas', {
                headers: { 'Authorization': `Bearer ${authToken}` }
            })
                .then(response => response.ok ? response.json() : Promise.reject('Falha ao buscar políticas'))
                .then(data => {
                    if (Array.isArray(data)) {
                        setPolicies(data);
                        if (data.length > 0) {
                            setSelectedPolicy(data[0].key);
                        }
                    }
                })
                .catch(error => console.error('Erro ao buscar políticas:', error));
        }
    }, [isAuthenticated, authToken]);


    const handleGeneratePassword = () => {
        if (!isAuthenticated) return;
        const apiUrl = '/api/gerar?length=16';
        fetch(apiUrl, {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${authToken}` }
        })
            .then(response => response.text())
            .then(data => setGeneratedPassword(data))
            .catch(error => console.error('Erro ao chamar a API de geração:', error));
    };


    const handleValidatePassword = (password, policyKey) => {
        setPasswordToValidate(password);
        if (!password || !policyKey || !isAuthenticated) {
            setValidationResult(null);
            return;
        }
        const apiUrl = `/api/validar?policyKey=${policyKey}`;
        fetch(apiUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${authToken}`},
            body: JSON.stringify({ password: password })
        })
            .then(response => response.json())
            .then(data => setValidationResult(data))
            .catch(error => console.error('Erro ao chamar a API de validação:', error));
    };


    return (

        <div className="main-layout">


            <div className="content-row">

                {/* Card do Gerador */}
                <div className="card">
                    <h2 style={{ fontSize: '2rem', fontWeight: 'bold', marginBottom: '1.5rem' }}>Gerador de Senhas</h2>
                    <input
                        type="text"
                        value={generatedPassword}
                        readOnly
                        style={{ marginBottom: '1rem' }}
                    />
                    <button onClick={handleGeneratePassword} disabled={!isAuthenticated}>
                        Gerar Nova Senha
                    </button>
                </div>

                {/* Card do Validador */}
                <div className="card">
                    <h2 style={{ fontSize: '2rem', fontWeight: 'bold', marginBottom: '1.5rem' }}>Validador de Força e Conformidade</h2>
                    <div className="policy-selector" style={{ marginBottom: '1.5rem' }}>
                        <label htmlFor="policy" style={{ marginRight: '1rem', color: '#9ca3af', fontWeight: '600' }}>
                            Escolha uma política:
                        </label>
                        <select
                            id="policy"
                            value={selectedPolicy}
                            onChange={(e) => {
                                const newPolicy = e.target.value;
                                setSelectedPolicy(newPolicy);
                                handleValidatePassword(passwordToValidate, newPolicy);
                            }}
                            disabled={!isAuthenticated}
                            style={{ backgroundColor: '#374151', padding: '0.5rem', borderRadius: '8px', border: '1px solid #4b5563', color: 'white' }}
                        >
                            {policies.map(policy => (
                                <option key={policy.key} value={policy.key}>{policy.name}</option>
                            ))}
                        </select>
                    </div>

                    <input
                        type="text"
                        placeholder={isAuthenticated ? "Digite uma senha para validar" : "Faça login para validar"}
                        value={passwordToValidate}
                        onChange={(e) => handleValidatePassword(e.target.value, selectedPolicy)}
                        disabled={!isAuthenticated}
                    />

                    {isAuthenticated && validationResult && passwordToValidate && (
                        <div className="validation-results" style={{ marginTop: '1.5rem', textAlign: 'left' }}>
                            <p>
                                <strong>Força:</strong> {validationResult.strength} |
                                <strong> Pontuação:</strong> {validationResult.score} |
                                <strong> Entropia:</strong> {validationResult.entropy.toFixed(2)} bits
                            </p>
                            {validationResult.isPwned && (<p className="error-message"><strong>PERIGO:</strong> Esta senha já foi exposta em um vazamento de dados!</p>)}
                            {validationResult.policyResult && (
                                <div className="policy-check">
                                    <h4 style={{ marginTop: '1rem' }}>Conformidade com a Política: {validationResult.policyResult.isValid ? '✅ Válida' : '❌ Inválida'}</h4>
                                    {!validationResult.policyResult.isValid && validationResult.policyResult.brokenRules.length > 0 && (
                                        <ul style={{ paddingLeft: '20px', listStyleType: 'disc' }}>
                                            {validationResult.policyResult.brokenRules.map((rule, index) => (<li key={index} className="error-message" style={{ color: '#fca5a5' }}>{rule}</li>))}
                                        </ul>
                                    )}
                                </div>
                            )}
                        </div>
                    )}
                </div>
            </div>

            <PasswordHistory />
        </div>
    );
}

export default HomePage;