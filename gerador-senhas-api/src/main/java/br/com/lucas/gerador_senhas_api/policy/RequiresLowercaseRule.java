package br.com.lucas.gerador_senhas_api.policy;

public class RequiresLowercaseRule implements PasswordPolicyRule{

    @Override
    public boolean test(String password){
        return password != null && password.matches(".*[a-z].*");
    }

    @Override
    public String getErrorMessage() {
        return "A senha deve conter pelo menos uma letra minúscula.";
    }
}
