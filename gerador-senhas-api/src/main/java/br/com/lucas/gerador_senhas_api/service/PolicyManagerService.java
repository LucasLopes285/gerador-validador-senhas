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

    private void buildDefaultPolicies(){
        PasswordPolicy corporateBasic = new PasswordPolicy(
                "Política Corporativa Básica",
                List.of(
                        new MinimumLengthRule(8),
                        new RequiresLowercaseRule(),
                        new RequiresUppercaseRule(),
                        new RequiresNumberRule(),
                        new RequiresSpecialCharacterRule()
                )
        );
        policies.put("CORPORATE_BASIC", corporateBasic);

        PasswordPolicy simple = new PasswordPolicy(
                "Política Simples (Sites Básicos)",
                List.of(
                        new MinimumLengthRule(6),
                        new RequiresLowercaseRule(),
                        new RequiresUppercaseRule()
                )
        );
        policies.put("SIMPLE", simple);

        // resto das politicas aqui
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
