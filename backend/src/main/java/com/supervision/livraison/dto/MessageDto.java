package com.supervision.livraison.dto;

import com.supervision.livraison.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long    id;
    private Long    senderId;
    private String  senderName;
    private Long    recipientId;
    private String  recipientName;
    private Long    nocde;
    private String  clientTel;
    private String  body;
    private String  type;
    private LocalDateTime createdAt;
    private boolean read;

    public static MessageDto from(Message m) {
        return MessageDto.builder()
                .id(m.getId())
                .senderId(m.getSender().getIdpers())
                .senderName(m.getSender().getPrenompers() + " " + m.getSender().getNompers())
                .recipientId(m.getRecipient().getIdpers())
                .recipientName(m.getRecipient().getPrenompers() + " " + m.getRecipient().getNompers())
                .nocde(m.getNocde())
                .clientTel(m.getClientTel())
                .body(m.getBody())
                .type(m.getType())
                .createdAt(m.getCreatedAt())
                .read(m.isRead())
                .build();
    }
}
