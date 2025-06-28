package br.com.lucas.gerador_senhas_api.policy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa um conjunto de regras que formam uma política de senha completa.
 */
public class PasswordPolicy {

    private final String policyName;
    private final List<PasswordPolicyRule> rules;

    public PasswordPolicy(String policyName, List<PasswordPolicyRule> rules){
        this.policyName = policyName;
        this.rules = rules;
    }

    public List<String> validate(String password){
        return this.rules.stream()
                .filter(rule -> !rule.test(password))
                .map(PasswordPolicyRule::getErrorMessage)
                .collect(Collectors.toList());
    }

    public String getPolicyName() {
        return policyName;
    }

    public List<PasswordPolicyRule> getRules() {
        return rules;
    }
}
