package br.com.lucas.gerador_senhas_api.policy;

public class MinimumLengthRule implements PasswordPolicyRule{

    private final int minLength;

    public MinimumLengthRule(int minLength){
        this.minLength = minLength;
    }

    @Override
    public boolean test(String password){
        if(password == null){
            return false;
        }
        return password.length() >= this.minLength;
    }
    @Override
    public String getErrorMessage(){
        return "A senha deve ter no mínimo " + this.minLength + " caracteres.";
    }
}
