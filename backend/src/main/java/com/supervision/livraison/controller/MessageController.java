package com.supervision.livraison.controller;

import com.supervision.livraison.dto.MessageDto;
import com.supervision.livraison.dto.SendMessageRequest;
import com.supervision.livraison.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Messaging endpoints.
 *
 * <ul>
 *   <li>POST /api/messages                   — send a message (INFO or EMERGENCY)</li>
 *   <li>GET  /api/messages/inbox/{userId}    — full inbox</li>
 *   <li>GET  /api/messages/unread/{userId}   — unread inbox (polled by the app)</li>
 *   <li>PUT  /api/messages/{id}/read         — mark as read</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public MessageDto send(@RequestBody SendMessageRequest req) {
        return messageService.send(req);
    }

    @GetMapping("/inbox/{userId}")
    public List<MessageDto> inbox(@PathVariable Long userId) {
        return messageService.inbox(userId);
    }

    @GetMapping("/unread/{userId}")
    public List<MessageDto> unread(@PathVariable Long userId) {
        return messageService.unread(userId);
    }

    @PutMapping("/{id}/read")
    public void markRead(@PathVariable Long id) {
        messageService.markRead(id);
    }
}
