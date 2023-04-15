package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.errors.PasswordNotMatchedException;
import com.codesoom.assignment.infra.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public String login(LoginRequestData loginRequestData) {
        String email = loginRequestData.getEmail();

        User user = userService.findByEmail(email);

        if (user.authenticate(loginRequestData.getPassword())) {
            throw new PasswordNotMatchedException(email);
        }

        return jwtUtils.encode(user.getId());
    }

    public Long parseToken(String token) {
        Claims claims = jwtUtils.decode(token);
        return claims.get("userId", Long.class);
    }
}
