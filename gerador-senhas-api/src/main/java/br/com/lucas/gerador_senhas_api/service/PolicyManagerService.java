package br.com.lucas.gerador_senhas_api.service;

import br.com.lucas.gerador_senhas_api.policy.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class PolicyManagerService {

    private final Map<String, PasswordPolicy> policies = new ConcurrentHashMap<>();

    public PolicyManagerService(){
        buildDefaultPolicies();
    }

    private void buildDefaultPolicies() {

        policies.clear();


        policies.put("CORPORATE_BASIC", new PasswordPolicy(
                "Corporativa Básica - Escritórios padrão",
                List.of(
                        new MinimumLengthRule(8),
                        new RequiresLowercaseRule(),
                        new RequiresUppercaseRule(),
                        new RequiresNumberRule(),
                        new RequiresSpecialCharacterRule()
                )
        ));


        policies.put("BANKING_HIGH_SEC", new PasswordPolicy(
                "Bancária - Alta segurança financeira",
                List.of(
                        new MinimumLengthRule(12),
                        new RequiresLowercaseRule(),
                        new RequiresUppercaseRule(),
                        new RequiresNumberRule(),
                        new RequiresSpecialCharacterRule()
                )
        ));


        policies.put("SIMPLE", new PasswordPolicy(
                "Simples - Sites básicos/pessoais",
                List.of(
                        new MinimumLengthRule(8),
                        new RequiresLowercaseRule(),
                        new RequiresNumberRule()
                )
        ));


        policies.put("GOVERNMENT_EXTREME", new PasswordPolicy(
                "Governamental - Máxima segurança",
                List.of(
                        new MinimumLengthRule(16),
                        new RequiresLowercaseRule(),
                        new RequiresUppercaseRule(),
                        new RequiresNumberRule(),
                        new RequiresSpecialCharacterRule()
                )
        ));


        policies.put("GAMING", new PasswordPolicy(
                "Gaming - Jogos e entretenimento",
                List.of(
                        new MinimumLengthRule(6),
                        new RequiresLowercaseRule(),
                        new RequiresUppercaseRule()

                )
        ));


        policies.put("SOCIAL_MEDIA", new PasswordPolicy(
                "Redes Sociais - Plataformas sociais",
                List.of(
                        new MinimumLengthRule(8),
                        new RequiresLowercaseRule(),
                        new RequiresNumberRule(),
                        new RequiresSpecialCharacterRule()
                )
        ));


        policies.put("IOT_ROUTERS", new PasswordPolicy(
                "IoT/Roteadores - Dispositivos conectados",
                List.of(
                        new MinimumLengthRule(8),
                        new RequiresLowercaseRule(),
                        new RequiresUppercaseRule(),
                        new RequiresNumberRule()

                )
        ));
    }


    public PasswordPolicy getPolicy(String policyKey){
        if(policyKey == null) return null;
        return policies.get(policyKey.toUpperCase());
    }

    public List<Map<String, String>> getAvailablePolicies(){
        return policies.entrySet().stream()
                .map(entry -> Map.of(
                        "key", entry.getKey(),
                        "name", entry.getValue().getPolicyName()
                ))
                .collect(Collectors.toList());
    }
}
