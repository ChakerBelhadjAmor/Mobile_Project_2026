package com.supervision.livraison.controller;

import com.supervision.livraison.dto.LoginRequest;
import com.supervision.livraison.dto.LoginResponse;
import com.supervision.livraison.service.AuthService;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication endpoints (login only — the app keeps the returned identity
 * client-side for subsequent requests).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
