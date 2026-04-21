package com.supervision.livraison.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Delivery of a {@link Commande} by a driver ({@link Personnel}).
 * The {@code etatliv} column is the primary field the controller monitors
 * and the driver updates in the field.
 */
@Entity
@Table(name = "livraison_com")
@Getter
@Setter
@NoArgsConstructor
public class LivraisonCom {

    /** Primary key shared with the underlying {@link Commande} via {@code @MapsId}. */
    @Id
    private Long nocde;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "nocde")
    private Commande commande;

    @Column(name = "dateliv")
    private LocalDate dateliv;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livreur")
    private Personnel livreur;

    /** Payment mode (e.g. ESPECES, CB, CHEQUE, VIREMENT). */
    @Column(name = "modepay", length = 20)
    private String modepay;

    /**
     * Delivery state. Allowed values used by the apps:
     * {@code EN_ATTENTE}, {@code EN_COURS}, {@code LIVREE}, {@code NON_LIVREE}.
     */
    @Column(name = "etatliv", length = 30)
    private String etatliv;

    /** Remark required when {@code etatliv == NON_LIVREE}. */
    @Column(name = "remarque", length = 500)
    private String remarque;
}
