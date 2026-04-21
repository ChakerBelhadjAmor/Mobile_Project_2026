package com.supervision.livraison.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Staff member (personnel). Can log in through the mobile app.
 * The role (controller vs. driver) is determined by the associated {@link Poste}.
 */
@Entity
@Table(name = "personnel",
       uniqueConstraints = @UniqueConstraint(columnNames = "login"))
@Getter
@Setter
@NoArgsConstructor
public class Personnel {

    @Id
    @Column(name = "idpers")
    private Long idpers;

    @Column(name = "nompers", length = 80)
    private String nompers;

    @Column(name = "prenompers", length = 80)
    private String prenompers;

    @Column(name = "adrpers", length = 200)
    private String adrpers;

    @Column(name = "villepers", length = 80)
    private String villepers;

    @Column(name = "telpers", length = 30)
    private String telpers;

    @Column(name = "d_embauche")
    private LocalDate dEmbauche;

    @Column(name = "login", length = 50, nullable = false)
    private String login;

    /**
     * Password hash. Stored in clear text only in the seed fixture for ease
     * of evaluation; a production deployment would use BCrypt.
     */
    @Column(name = "mot_p", length = 200, nullable = false)
    private String motP;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codeposte")
    private Poste poste;
}
