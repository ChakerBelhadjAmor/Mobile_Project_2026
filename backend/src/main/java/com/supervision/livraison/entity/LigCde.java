package com.supervision.livraison.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Order line: a quantity of a given {@link Article} on a given {@link Commande}.
 */
@Entity
@Table(name = "lig_cdes")
@Getter
@Setter
@NoArgsConstructor
public class LigCde {

    @EmbeddedId
    private LigCdeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("nocde")
    @JoinColumn(name = "nocde")
    private Commande commande;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("refart")
    @JoinColumn(name = "refart")
    private Article article;

    @Column(name = "qtecde", nullable = false)
    private Integer qtecde;
}
