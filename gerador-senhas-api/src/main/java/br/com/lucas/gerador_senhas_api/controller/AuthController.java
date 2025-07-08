package br.com.lucas.gerador_senhas_api.controller;

import br.com.lucas.gerador_senhas_api.dto.LoginRequest;
import br.com.lucas.gerador_senhas_api.dto.LoginResponse;
import br.com.lucas.gerador_senhas_api.dto.RegisterRequest;
import br.com.lucas.gerador_senhas_api.dto.SecondFactorRequest;
import br.com.lucas.gerador_senhas_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest){
        try{
            authService.registerUser(registerRequest.email(), registerRequest.password());
            return ResponseEntity.ok("Usuário registrado com sucesso!");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        String token = authService.loginUser(loginRequest.email(), loginRequest.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/set-second-factor")
    public ResponseEntity<String> setSecondFactor(@AuthenticationPrincipal UserDetails userDetails, @RequestBody SecondFactorRequest request) {
        try{
            authService.setSecondFactorPassword(userDetails.getUsername(), request.password());
            return ResponseEntity.ok("Senha de segurança para o histórico cadastrada com sucesso.");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
