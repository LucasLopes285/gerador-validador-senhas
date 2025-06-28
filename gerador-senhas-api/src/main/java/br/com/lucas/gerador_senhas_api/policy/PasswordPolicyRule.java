package br.com.lucas.gerador_senhas_api.policy;

public interface PasswordPolicyRule {

    /**
     * Testa se a senha fornecida passa nesta regra específica.
     * @param password A senha a ser testada.
     * @return true se a senha for válida de acordo com a regra, false caso contrário.
     */
    boolean test(String password);

    /**
     * Retorna a mensagem de erro que deve ser exibida se a senha falhar na regra.
     * @return A mensagem de erro.
     */
    String getErrorMessage();
}
