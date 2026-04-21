package com.supervision.livraison.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Job position (poste) held by a staff member.
 * The {@code libelle} drives authorization — typical values are
 * {@code CONTROLEUR} and {@code LIVREUR}.
 */
@Entity
@Table(name = "postes")
@Getter
@Setter
@NoArgsConstructor
public class Poste {

    @Id
    @Column(name = "codeposte", length = 20)
    private String codeposte;

    @Column(name = "libelle", length = 80, nullable = false)
    private String libelle;

    @Column(name = "indice")
    private Integer indice;
}
