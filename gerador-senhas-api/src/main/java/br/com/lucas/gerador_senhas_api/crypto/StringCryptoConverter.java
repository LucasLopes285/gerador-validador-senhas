package br.com.lucas.gerador_senhas_api.crypto;

import jakarta.persistence.AttributeConverter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class StringCryptoConverter  implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final byte[] KEY = "MySuperSecretKey".getBytes();

    @Override
    public String convertToDatabaseColumn(String attribute){
        // Este método CRIPTOGRAFA o dado ANTES de salvar no banco.
        if(attribute == null){
            return null;
        }
        try{
            Key key = new SecretKeySpec(KEY, "AES");
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(c.doFinal(attribute.getBytes()));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar o atributo", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData){
        // Este método DESCRIPTOGRAFA o dado DEPOIS de ler do banco.
        if(dbData == null){
            return null;
        }
        try {
            Key key = new SecretKeySpec(KEY, "AES");
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            return new String(c.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar o atributo", e);
        }
    }
}
