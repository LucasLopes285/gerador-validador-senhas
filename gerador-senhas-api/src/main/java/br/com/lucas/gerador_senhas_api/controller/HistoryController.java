package br.com.lucas.gerador_senhas_api.controller;

import br.com.lucas.gerador_senhas_api.dto.ValidationRequest;
import br.com.lucas.gerador_senhas_api.model.PasswordHistory;
import br.com.lucas.gerador_senhas_api.repository.PasswordHistoryRepository;
import br.com.lucas.gerador_senhas_api.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historico")
public class HistoryController {

    @Autowired
    private PasswordHistoryRepository historyRepository;

    @Autowired
    private HistoryService historyService;

    @GetMapping
    public List<PasswordHistory> getHistory(){
        return historyRepository.findAllByOrderByIdDesc();
    }

    @PostMapping
    public ResponseEntity<String> savePasswordToHistory(@RequestBody ValidationRequest request) {
        historyService.savePassword(request.password());
        return ResponseEntity.ok("Senha salva no histórico com sucesso.");
    }
}
