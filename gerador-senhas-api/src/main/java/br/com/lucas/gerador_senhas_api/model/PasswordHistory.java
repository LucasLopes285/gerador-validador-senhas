package br.com.lucas.gerador_senhas_api.model;

import br.com.lucas.gerador_senhas_api.crypto.StringCryptoConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_history")
public class PasswordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = StringCryptoConverter.class)
    @Column(name = "password_value", nullable = false)
    private String passwordValue;

    @Column(name = "created_id", nullable = false)
    private LocalDateTime createdAt;

    public PasswordHistory() {}

    public PasswordHistory(String passwordValue){
        this.passwordValue = passwordValue;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {

    }

    public String getPasswordValue() {
        return passwordValue;
    }

    public void setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
