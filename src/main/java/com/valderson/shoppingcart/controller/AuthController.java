package com.valderson.shoppingcart.controller;

import com.valderson.shoppingcart.dto.request.LoginRequest;
import com.valderson.shoppingcart.dto.request.RegisterRequest;
import com.valderson.shoppingcart.dto.response.UserResponse;
import com.valderson.shoppingcart.security.JwtTokenProvider;
import com.valderson.shoppingcart.service.AuthService;
import com.valderson.shoppingcart.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request,
                                                 HttpServletResponse response) {
        try {
            UserResponse user = authService.register(request);

            // Gerar token JWT
            String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());

            // Adicionar cookie HTTP-only
            cookieUtil.addAuthCookie(response, token);

            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro no registro: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletResponse response) {
        try {
            UserResponse user = authService.login(request);

            // Gerar token JWT
            String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());

            // Adicionar cookie HTTP-only
            cookieUtil.addAuthCookie(response, token);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro no login: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // Limpar cookie
        cookieUtil.clearAuthCookie(response);
        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            String email = (String) request.getAttribute("userEmail");

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Token inválido ou usuário não autenticado");
            }

            UserResponse user = authService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar dados do usuário: " + e.getMessage());
        }
    }
}