package br.com.lucas.gerador_senhas_api.controller;

import br.com.lucas.gerador_senhas_api.dto.ValidationRequest;
import br.com.lucas.gerador_senhas_api.dto.ValidationResponse;
import br.com.lucas.gerador_senhas_api.service.PasswordGeneratorService;
import br.com.lucas.gerador_senhas_api.service.PasswordValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PasswordController {


    @Autowired
    private PasswordGeneratorService passwordService;
    @Autowired
    private PasswordValidatorService validatorService;

    @GetMapping("/ola")
    public String dizerOla(){
        return "Olá, mundo! A minha primeira API REST está no ar!";
    }

    @GetMapping("/gerar")
    public String gerarSenha(
        @RequestParam(value = "length", defaultValue = "12") int length,
        @RequestParam(value = "includeUppercase", defaultValue = "true") boolean includeUppercase,
        @RequestParam(value = "includeDigits", defaultValue = "true") boolean includeDigits,
        @RequestParam(value = "includeSpecial", defaultValue = "true") boolean includeSpecial
    ) {

        return passwordService.generatePassword(length, includeUppercase, includeDigits, includeSpecial);
    }

    @PostMapping("/validar")
    public ValidationResponse validarSenha(@RequestBody ValidationRequest request){
        return validatorService.validatePassword(request.password());
    }
}
