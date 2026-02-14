package br.com.personalfinance.auth;

import br.com.personalfinance.auth.dto.LoginRequest;
import br.com.personalfinance.auth.dto.SignupRequest;
import br.com.personalfinance.auth.entity.User;
import br.com.personalfinance.common.exception.BusinessException;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @Transactional
    public User signup(SignupRequest request) throws BusinessException {
        if (User.findByEmail(request.email()) != null) {
            throw new BusinessException("Email already registered");
        }

        User user = new User();
        user.name = request.name();
        user.email = request.email();
        user.password = BcryptUtil.bcryptHash(request.password());
        user.createdAt = LocalDateTime.now();
        user.persist();

        return user;
    }

    public User authenticate(LoginRequest request) throws BusinessException {
        User user = User.findByEmail(request.email());

        if (user == null || !BcryptUtil.matches(request.password(), user.password)) {
            throw new BusinessException("Invalid email or password");
        }

        return user;
    }

    public String generateToken(User user) {
        return Jwt.issuer("personal-finance-auth")
                .upn(user.email)
                .claim("name", user.name)
                .claim("userId", user.id)
                .groups(Set.of("user"))
                .expiresIn(3600)
                .sign();
    }
}
