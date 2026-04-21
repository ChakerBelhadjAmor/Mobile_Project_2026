package com.supervision.livraison.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Customer order (commande). Linked to a {@link Client} and composed of
 * one or more {@link LigCde} lines.
 */
@Entity
@Table(name = "commandes")
@Getter
@Setter
@NoArgsConstructor
public class Commande {

    @Id
    @Column(name = "nocde")
    private Long nocde;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "noclt", nullable = false)
    private Client client;

    @Column(name = "datecde")
    private LocalDate datecde;

    /** Order state (e.g. CREEE, CONFIRMEE, ANNULEE). */
    @Column(name = "etatcde", length = 30)
    private String etatcde;

    @OneToMany(mappedBy = "commande", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LigCde> lignes = new ArrayList<>();
}
