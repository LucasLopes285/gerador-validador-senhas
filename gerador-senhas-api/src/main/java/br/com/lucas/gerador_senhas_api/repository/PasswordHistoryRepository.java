package br.com.lucas.gerador_senhas_api.repository;

import br.com.lucas.gerador_senhas_api.model.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.lucas.gerador_senhas_api.model.User;

import java.util.List;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    List<PasswordHistory> findByUserOrderByIdDesc(User user);
}
