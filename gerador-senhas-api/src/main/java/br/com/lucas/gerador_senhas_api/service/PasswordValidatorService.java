
package br.com.lucas.gerador_senhas_api.service;

import br.com.lucas.gerador_senhas_api.dto.ValidationResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordValidatorService {

    public ValidationResponse validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return new ValidationResponse(0, "Inválida", 0.0, List.of("Senha não pode ser vazia."));
        }

        List<String> suggestions = new ArrayList<>();
        int score = 0;

        int characterPoolSize = 0;
        if (password.matches(".*[a-z].*")) {
            characterPoolSize += 26;
        }
        if (password.matches(".*[A-Z].*")) {
            characterPoolSize += 26;
        }
        if (password.matches(".*[0-9].*")) {
            characterPoolSize += 10;
        }
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?].*")) {
            characterPoolSize += 32;
        }

        double entropy = 0.0;
        if (characterPoolSize > 0) {
            entropy = password.length() * (Math.log(characterPoolSize) / Math.log(2));
        }

        if (password.length() < 8) {
            suggestions.add("Muito curta. Tente ter pelo menos 8 caracteres.");
        } else if (password.length() < 12) {
            score += 25;
            suggestions.add("Bom tamanho. Para senhas mais fortes, use 12 ou mais caracteres.");
        } else {
            score += 50;
            suggestions.add("Excelente tamanho.");
        }

        if (password.matches(".*[A-Z].*")) {
            score += 20;
            suggestions.add("Contém letras maiúsculas.");
        } else {
            suggestions.add("Adicione pelo menos uma letra maiúscula.");
        }

        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?].*")) {
            score += 15;
            suggestions.add("Contém caracteres especiais.");
        } else {
            suggestions.add("Use caracteres especiais como !@#$% etc.");
        }

        String strength = "Fraca";
        if (score >= 85) {
            strength = "Muito Forte";
        } else if (score >= 60) {
            strength = "Forte";
        } else if (score >= 40) {
            strength = "Média";
        }

        return new ValidationResponse(score, strength, entropy, suggestions);
    }
}