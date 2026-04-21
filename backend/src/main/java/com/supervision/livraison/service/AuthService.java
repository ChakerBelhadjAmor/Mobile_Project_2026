package com.supervision.livraison.service;

import com.supervision.livraison.dto.LoginRequest;
import com.supervision.livraison.dto.LoginResponse;
import com.supervision.livraison.entity.Personnel;
import com.supervision.livraison.repository.PersonnelRepository;
import org.springframework.stereotype.Service;

/**
 * Lightweight login service. Verifies the clear-text password stored on
 * {@link Personnel#getMotP()} — sufficient for the academic scope; swap for
 * BCrypt in a real deployment.
 */
@Service
public class AuthService {

    private final PersonnelRepository personnelRepository;

    public AuthService(PersonnelRepository personnelRepository) {
        this.personnelRepository = personnelRepository;
    }

    public LoginResponse login(LoginRequest req) {
        Personnel p = personnelRepository.findByLogin(req.getLogin())
                .orElseThrow(() -> new IllegalArgumentException("Identifiants invalides"));
        if (!p.getMotP().equals(req.getMotP())) {
            throw new IllegalArgumentException("Identifiants invalides");
        }
        return LoginResponse.from(p);
    }
}
