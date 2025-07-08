package br.com.lucas.gerador_senhas_api.controller;

import br.com.lucas.gerador_senhas_api.dto.SaveHistoryRequest;
import br.com.lucas.gerador_senhas_api.dto.ViewHistoryRequest;
import br.com.lucas.gerador_senhas_api.model.PasswordHistory;
import br.com.lucas.gerador_senhas_api.model.User;
import br.com.lucas.gerador_senhas_api.repository.PasswordHistoryRepository;
import br.com.lucas.gerador_senhas_api.repository.UserRepository;
import br.com.lucas.gerador_senhas_api.service.AuthService;
import br.com.lucas.gerador_senhas_api.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/historico")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;


    @PostMapping("/view")
    public ResponseEntity<?> viewHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ViewHistoryRequest request) {

        try {

            boolean isSecondFactorValid = authService.verifySecondFactorPassword(userDetails.getUsername(), request.secondPassword());

            if (isSecondFactorValid) {

                Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
                if (userOptional.isPresent()) {
                    List<PasswordHistory> history = passwordHistoryRepository.findByUserOrderByIdDesc(userOptional.get());
                    return ResponseEntity.ok(history); // Retorna a lista com status 200 OK
                }
                return ResponseEntity.ok(Collections.emptyList());
            } else {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha de segurança inválida.");
            }
        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/save")
    public ResponseEntity<String> savePasswordToHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SaveHistoryRequest request) {


        userRepository.findByEmail(userDetails.getUsername()).ifPresent(user -> {
            historyService.savePassword(request.password(), user);
        });

        return ResponseEntity.ok("Senha salva no histórico com sucesso.");
    }
}