package com.supervision.livraison.repository;

import com.supervision.livraison.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRecipient_IdpersOrderByCreatedAtDesc(Long recipientId);

    List<Message> findByRecipient_IdpersAndReadFalseOrderByCreatedAtDesc(Long recipientId);
}
