package com.supervision.livraison.dto;

import lombok.Data;

/**
 * Payload sent by a driver when updating the state of a delivery.
 * {@code remarque} is mandatory when {@code etatliv = NON_LIVREE}.
 */
@Data
public class UpdateEtatRequest {
    private String etatliv;
    private String remarque;
}
