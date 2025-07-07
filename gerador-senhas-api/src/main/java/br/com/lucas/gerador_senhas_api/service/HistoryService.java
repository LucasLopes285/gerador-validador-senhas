package br.com.lucas.gerador_senhas_api.service;

import br.com.lucas.gerador_senhas_api.model.PasswordHistory;
import br.com.lucas.gerador_senhas_api.repository.PasswordHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    @Autowired
    private PasswordHistoryRepository historyRepository;

    public void savePassword(String password){
        if(password == null || password.isEmpty()){
            return;
        }
        PasswordHistory historyRecord = new PasswordHistory(password);
        historyRepository.save(historyRecord);
    }
}
