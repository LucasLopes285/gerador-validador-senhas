package br.com.lucas.gerador_senhas_api.policy;

public class RequiresNumberRule implements PasswordPolicyRule {

    @Override
    public boolean test(String password){
        return password != null && password.matches(".*[0-9].*");
    }

    @Override
    public String getErrorMessage() {
        return "A senha deve conter pelo menos um número.";
    }

}
