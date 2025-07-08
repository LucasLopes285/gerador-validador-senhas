package br.com.lucas.gerador_senhas_api.controller;

import br.com.lucas.gerador_senhas_api.dto.ValidationRequest;
import br.com.lucas.gerador_senhas_api.model.PasswordHistory;
import br.com.lucas.gerador_senhas_api.repository.PasswordHistoryRepository;
import br.com.lucas.gerador_senhas_api.repository.UserRepository;
import br.com.lucas.gerador_senhas_api.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/historico")
public class HistoryController {

    @Autowired
    private PasswordHistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistoryService historyService;

    @GetMapping
    public List<PasswordHistory> getHistory(@AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        return userRepository.findByEmail(email)
                .map(historyRepository::findByUserOrderByIdDesc)
                .orElse(Collections.emptyList());
    }

    @PostMapping
    public ResponseEntity<String> savePasswordToHistory(@RequestBody String password, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        userRepository.findByEmail(email).ifPresent(user -> {
            historyService.savePassword(password, user);
        });
        return ResponseEntity.ok("Senha salva no histórico com sucesso.");
    }
}
