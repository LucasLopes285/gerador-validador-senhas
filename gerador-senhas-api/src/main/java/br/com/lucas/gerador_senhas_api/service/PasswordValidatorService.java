package br.com.lucas.gerador_senhas_api.service;


import br.com.lucas.gerador_senhas_api.dto.PolicyValidationResult;
import br.com.lucas.gerador_senhas_api.dto.ValidationResponse;
import br.com.lucas.gerador_senhas_api.policy.PasswordPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PasswordValidatorService {


    @Autowired
    private PolicyManagerService policyManagerService;


    public ValidationResponse validatePassword(String password, String policyKey) {
        if (password == null || password.trim().isEmpty()) {
            // Ajustando a resposta para corresponder ao novo DTO
            return new ValidationResponse(0, "Inválida", 0.0, false, List.of("Senha não pode ser vazia."), new PolicyValidationResult(false, List.of("Senha não pode ser vazia.")));
        }

        List<String> suggestions = new ArrayList<>();
        int score = 0;
        String strength = "Fraca";

        // Análise de Vazamentos (Pwned Passwords)
        boolean isPwned = isPasswordPwned(password);
        if (isPwned) {
            score = 0;
            strength = "Comprometida";
            suggestions.add("PERIGO: Esta senha já foi exposta em um vazamento de dados! Não a utilize.");
        } else {
            // (Lógica de pontuação por tamanho, maiúsculas, etc., que já tínhamos)
            if (password.length() < 8) {
                suggestions.add("Muito curta. Tente ter pelo menos 8 caracteres.");
            } else if (password.length() < 12) {
                score += 25;
                suggestions.add("Bom tamanho. Para senhas mais fortes, use 12 ou mais caracteres.");
            } else {
                score += 50;
                suggestions.add("Excelente tamanho.");
            }
            if (password.matches(".*[A-Z].*")) { score += 20; suggestions.add("Contém letras maiúsculas."); } else { suggestions.add("Adicione pelo menos uma letra maiúscula."); }
            if (password.matches(".*[0-9].*")) { score += 20; suggestions.add("Contém números."); } else { suggestions.add("Adicione números para aumentar a força."); }
            if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?].*")) { score += 25; suggestions.add("Contém caracteres especiais."); } else { suggestions.add("Use caracteres especiais como !@#$% etc."); }

            if(containsSequence(password)){
                score -= 30;
                suggestions.add("Evite sequências óbvias como 'abc' ou '123'.");
            }
            if(containsRepetition(password)){
                score -= 30;
                suggestions.add("Evite caracteres repetidos como 'aaaa' ou '1111'.");
            }

            if(score < 0) score = 0;

            if (score >= 85) { strength = "Muito Forte"; } else if (score >= 60) { strength = "Forte"; } else if (score >= 40) { strength = "Média"; }
        }

        // Cálculo de Entropia
        int characterPoolSize = 0;
        if (password.matches(".*[a-z].*")) characterPoolSize += 26;
        if (password.matches(".*[A-Z].*")) characterPoolSize += 26;
        if (password.matches(".*[0-9].*")) characterPoolSize += 10;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?].*")) characterPoolSize += 32;
        double entropy = (characterPoolSize > 0) ? password.length() * (Math.log(characterPoolSize) / Math.log(2)) : 0.0;

        // Validação da Política
        PolicyValidationResult policyResult;
        PasswordPolicy policy = policyManagerService.getPolicy(policyKey);
        if (policy != null) {
            List<String> brokenRules = policy.validate(password);
            policyResult = new PolicyValidationResult(brokenRules.isEmpty(), brokenRules);
        } else {
            policyResult = new PolicyValidationResult(true, Collections.emptyList());
        }

        // Retorna a resposta completa, agora com todos os 6 campos do DTO
        return new ValidationResponse(score, strength, entropy, isPwned, suggestions, policyResult);
    }

    private boolean containsSequence(String password) {
        String lowerCasePassword = password.toLowerCase();

        String[] sequences = {"12345", "23456", "34567", "45678", "56789", "01234", "abcde", "bcdef", "cdefg", "qwerty", "asdfg", "zxcvb"};
        for(String seq : sequences){
            if(lowerCasePassword.contains(seq)){
                return true;
            }
        }
        return false;
    }

    private boolean containsRepetition(String password){
        return password.matches(".*(.)\\1{3,}.*");
    }


    private boolean isPasswordPwned(String password) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            String sha1Hash = hexString.toString().toUpperCase();

            String hashPrefix = sha1Hash.substring(0, 5);
            String hashSuffix = sha1Hash.substring(5);

            String apiUrl = "https://api.pwnedpasswords.com/range/" + hashPrefix;
            RestTemplate restTemplate = new RestTemplate();
            String apiResponse = restTemplate.getForObject(apiUrl, String.class);

            if (apiResponse != null) {
                return apiResponse.lines().anyMatch(line -> line.startsWith(hashSuffix));
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erro ao verificar senha vazada: " + e.getMessage());
            return false;
        }
    }
}