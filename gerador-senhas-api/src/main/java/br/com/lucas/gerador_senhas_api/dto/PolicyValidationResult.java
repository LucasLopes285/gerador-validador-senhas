package br.com.lucas.gerador_senhas_api.dto;

import java.util.List;

public record PolicyValidationResult(boolean isValid, List<String> brokenRules) {
}
