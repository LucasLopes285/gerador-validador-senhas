package br.com.lucas.gerador_senhas_api.service;

import br.com.lucas.gerador_senhas_api.model.PasswordHistory;
import br.com.lucas.gerador_senhas_api.repository.PasswordHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordGeneratorService {

    private static final String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPERCASE = CHAR_LOWERCASE.toUpperCase();
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+-=[]{}|;:,./<>?";

    private static final SecureRandom random = new SecureRandom();

    @Autowired
    private PasswordHistoryRepository historyRepository;

    public String generatePassword(int length, boolean includeUppercase, boolean includeDigits, boolean includeSpecial) {
        StringBuilder password = new StringBuilder(length);
        List<String> charCategories = new ArrayList<>();

        //começamos com letras minusculas como base
        charCategories.add(CHAR_LOWERCASE);
        if(includeUppercase){
            charCategories.add(CHAR_UPPERCASE);
        }
        if(includeDigits){
            charCategories.add(NUMBERS);
        }
        if(includeSpecial){
            charCategories.add(SPECIAL_CHARACTERS);
        }

        // Constrói a senha pegando um caractere de cada categoria escolhida
        for (int i = 0; i < length; i++) {
            String charCategory = charCategories.get(random.nextInt(charCategories.size()));
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));

        }

        String generatedPassword = password.toString();

        PasswordHistory historyRecord = new PasswordHistory(generatedPassword);

        historyRepository.save(historyRecord);

        return generatedPassword;
    }
}
