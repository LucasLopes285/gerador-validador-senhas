package br.com.lucas.gerador_senhas_api.service;

import br.com.lucas.gerador_senhas_api.model.User;
import br.com.lucas.gerador_senhas_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private  JwtService jwtService;

    public void registerUser(String email, String password){
        if(userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("Erro: O e-mail informado já está cadastrado.");
        }

        User newUser = new User();
        newUser.setEmail(email);

        String hashedPassword = passwordEncoder.encode(password);
        newUser.setPassword(hashedPassword);

        userRepository.save(newUser);


    }

    public String loginUser(String email, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(email);
        } else {
        throw new RuntimeException("Erro ao tentar fazer login.");
        }
    }

    public void setSecondFactorPassword(String email, String secondPassword){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        user.setSecondFactorPassword(passwordEncoder.encode(secondPassword));
        userRepository.save(user);
    }

    public boolean verifySecondFactorPassword(String email, String rawSecondPassword) {
       User user = userRepository.findByEmail(email)
               .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

       String storeHashedPassword = user.getSecondFactorPassword();

        if (storeHashedPassword == null || storeHashedPassword.isEmpty()) {
            throw new RuntimeException("Senha de segurança não cadastrada.");
        }
        return passwordEncoder.matches(rawSecondPassword, storeHashedPassword);
    }


}
