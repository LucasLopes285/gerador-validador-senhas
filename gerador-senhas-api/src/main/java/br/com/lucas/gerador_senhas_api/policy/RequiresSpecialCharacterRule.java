package br.com.lucas.gerador_senhas_api.policy;

public class RequiresSpecialCharacterRule implements PasswordPolicyRule{

    @Override
    public boolean test(String password){
        return password != null && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?].*");
    }

    @Override
    public String getErrorMessage() {
        return "A senha deve conter pelo menos um caractere especial (ex: !@#$).";
    }

}
