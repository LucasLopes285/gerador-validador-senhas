package br.com.lucas.gerador_senhas_api.service;

import br.com.lucas.gerador_senhas_api.model.PasswordHistory;
import br.com.lucas.gerador_senhas_api.repository.PasswordHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.lucas.gerador_senhas_api.model.User;

@Service
public class HistoryService {

    @Autowired
    private PasswordHistoryRepository historyRepository;

    public void savePassword(String password, User user) {
        if (password == null || password.isEmpty() || user == null) {
            return;
        }
        PasswordHistory historyRecord = new PasswordHistory(password, user);
        historyRepository.save(historyRecord);
    }
}
