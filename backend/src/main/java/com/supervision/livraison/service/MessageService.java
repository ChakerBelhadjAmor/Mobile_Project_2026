package com.supervision.livraison.service;

import com.supervision.livraison.dto.MessageDto;
import com.supervision.livraison.dto.SendMessageRequest;
import com.supervision.livraison.entity.LivraisonCom;
import com.supervision.livraison.entity.Message;
import com.supervision.livraison.entity.Personnel;
import com.supervision.livraison.repository.LivraisonRepository;
import com.supervision.livraison.repository.MessageRepository;
import com.supervision.livraison.repository.PersonnelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Handles the asynchronous messaging flow between controllers and drivers.
 *
 * <ul>
 *   <li>INFO: controller → driver (real-time information to drivers on tour).</li>
 *   <li>EMERGENCY: driver → controller, attached to a specific order, auto-fills
 *       the client phone number for controller convenience.</li>
 * </ul>
 */
@Service
public class MessageService {

    private final MessageRepository     messageRepository;
    private final PersonnelRepository   personnelRepository;
    private final LivraisonRepository   livraisonRepository;

    public MessageService(MessageRepository messageRepository,
                          PersonnelRepository personnelRepository,
                          LivraisonRepository livraisonRepository) {
        this.messageRepository   = messageRepository;
        this.personnelRepository = personnelRepository;
        this.livraisonRepository = livraisonRepository;
    }

    @Transactional
    public MessageDto send(SendMessageRequest req) {
        Personnel sender    = personnelRepository.findById(req.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Expéditeur inconnu"));
        Personnel recipient = personnelRepository.findById(req.getRecipientId())
                .orElseThrow(() -> new IllegalArgumentException("Destinataire inconnu"));

        Message m = new Message();
        m.setSender(sender);
        m.setRecipient(recipient);
        m.setBody(req.getBody());
        m.setType(req.getType() == null ? "INFO" : req.getType().toUpperCase());
        m.setNocde(req.getNocde());
        m.setClientTel(req.getClientTel());

        // For an EMERGENCY linked to an order, copy the client phone automatically
        // so the controller has everything to call back without another lookup.
        if ("EMERGENCY".equals(m.getType())
                && m.getClientTel() == null
                && req.getNocde() != null) {
            LivraisonCom l = livraisonRepository.findById(req.getNocde()).orElse(null);
            if (l != null && l.getCommande() != null && l.getCommande().getClient() != null) {
                m.setClientTel(l.getCommande().getClient().getTelclt());
            }
        }
        return MessageDto.from(messageRepository.save(m));
    }

    @Transactional(readOnly = true)
    public List<MessageDto> inbox(Long recipientId) {
        return messageRepository.findByRecipient_IdpersOrderByCreatedAtDesc(recipientId)
                .stream().map(MessageDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MessageDto> unread(Long recipientId) {
        return messageRepository.findByRecipient_IdpersAndReadFalseOrderByCreatedAtDesc(recipientId)
                .stream().map(MessageDto::from).toList();
    }

    @Transactional
    public void markRead(Long id) {
        messageRepository.findById(id).ifPresent(m -> { m.setRead(true); messageRepository.save(m); });
    }
}
