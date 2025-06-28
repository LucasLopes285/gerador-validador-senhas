package br.com.lucas.gerador_senhas_api.dto;

import java.util.List;

public record ValidationResponse(int score, String strength, double entropy,  boolean isPwned,  List<String> suggestions, PolicyValidationResult policyResult) {
}