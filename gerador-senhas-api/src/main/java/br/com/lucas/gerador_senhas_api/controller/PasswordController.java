package br.com.lucas.gerador_senhas_api.controller;

import br.com.lucas.gerador_senhas_api.dto.ValidationRequest;
import br.com.lucas.gerador_senhas_api.dto.ValidationResponse;
import br.com.lucas.gerador_senhas_api.model.PasswordHistory;
import br.com.lucas.gerador_senhas_api.repository.PasswordHistoryRepository;
import br.com.lucas.gerador_senhas_api.service.PasswordGeneratorService;
import br.com.lucas.gerador_senhas_api.service.PasswordValidatorService;
import br.com.lucas.gerador_senhas_api.service.PolicyManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PasswordController {


    @Autowired
    private PasswordGeneratorService passwordService;

    @Autowired
    private PasswordValidatorService validatorService;

    @Autowired
    private PasswordHistoryRepository historyRepository;

    @Autowired
    private PolicyManagerService policyManagerService;

    @GetMapping("/gerar")
    public String gerarSenha(
        @RequestParam(value = "length", defaultValue = "12") int length,
        @RequestParam(value = "includeUppercase", defaultValue = "true") boolean includeUppercase,
        @RequestParam(value = "includeDigits", defaultValue = "true") boolean includeDigits,
        @RequestParam(value = "includeSpecial", defaultValue = "true") boolean includeSpecial
    ) {

        return passwordService.generatePassword(length, includeUppercase, includeDigits, includeSpecial);
    }

    @GetMapping("/politicas")
    public List<Map<String, String>> getPolicies(){
        return policyManagerService.getAvailablePolicies();
    }

    @PostMapping("/validar")
    public ValidationResponse validarSenha(
            @RequestBody ValidationRequest request,
            @RequestParam(required = false) String policyKey) {

        return validatorService.validatePassword(request.password(), policyKey);
    }
}
