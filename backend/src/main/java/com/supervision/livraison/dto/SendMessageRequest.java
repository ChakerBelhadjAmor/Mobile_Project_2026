package com.supervision.livraison.dto;

import lombok.Data;

/**
 * Payload used by both the controller (INFO to a driver) and the driver
 * (EMERGENCY to a controller, linked to a specific order).
 */
@Data
public class SendMessageRequest {
    private Long    senderId;
    private Long    recipientId;
    private Long    nocde;       // optional
    private String  clientTel;   // optional, auto-filled from order when absent
    private String  body;
    private String  type;        // INFO or EMERGENCY
}
