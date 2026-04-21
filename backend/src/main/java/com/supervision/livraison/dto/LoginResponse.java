package com.supervision.livraison.dto;

import com.supervision.livraison.entity.Personnel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result of a successful login. The {@code role} field is derived from the
 * associated {@link com.supervision.livraison.entity.Poste}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long   idpers;
    private String nom;
    private String prenom;
    private String login;
    private String role;    // CONTROLEUR or LIVREUR

    public static LoginResponse from(Personnel p) {
        String role = p.getPoste() != null ? p.getPoste().getLibelle() : "INCONNU";
        return new LoginResponse(p.getIdpers(), p.getNompers(), p.getPrenompers(), p.getLogin(), role);
    }
}
