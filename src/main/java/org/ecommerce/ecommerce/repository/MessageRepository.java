package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiver(Long sender, Long receiver);
    List<Message> findByReceiverAndIsReadFalse(Long receiver);
}