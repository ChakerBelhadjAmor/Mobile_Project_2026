package com.supervision.livraison.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Client to whom orders are delivered.
 * Maps the {@code Clients} table.
 */
@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
public class Client {

    /** Client number (primary key). */
    @Id
    @Column(name = "noclt")
    private Long noclt;

    @Column(name = "nomclt", length = 80)
    private String nomclt;

    @Column(name = "prenomclt", length = 80)
    private String prenomclt;

    @Column(name = "adrclt", length = 200)
    private String adrclt;

    @Column(name = "villeclt", length = 80)
    private String villeclt;

    @Column(name = "code_postal", length = 20)
    private String codePostal;

    /** Phone number used by drivers to contact the client. */
    @Column(name = "telclt", length = 30)
    private String telclt;

    @Column(name = "adrmail", length = 120)
    private String adrmail;
}
