package com.supervision.livraison.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Asynchronous message exchanged between a controller and a driver.
 *
 * Supports two flows required by the spec:
 * <ul>
 *   <li>Controller → driver: real-time information to a driver on tour.</li>
 *   <li>Driver → controller: emergency message referring to a specific order.</li>
 * </ul>
 *
 * The "real-time" requirement is met through short-interval polling from the
 * mobile clients; the table is used as the persistent inbox.
 */
@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", nullable = false)
    private Personnel sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Personnel recipient;

    /** Optional linked order — used for driver emergency messages. */
    @Column(name = "nocde")
    private Long nocde;

    /** Optional client phone copied in the message for quick controller action. */
    @Column(name = "client_tel", length = 30)
    private String clientTel;

    @Column(name = "body", nullable = false, length = 1000)
    private String body;

    /**
     * Message type. Allowed: {@code INFO} (controller → driver),
     * {@code EMERGENCY} (driver → controller).
     */
    @Column(name = "type", length = 20, nullable = false)
    private String type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_flag", nullable = false)
    private boolean read;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
