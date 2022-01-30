package com.hust.webchat.repository;

import com.hust.webchat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("from Message m where (m.senderId = :senderId and m.receiverId = :receiverId) " +
            "or (m.senderId = :receiverId and m.receiverId = :senderId)")
    List<Message> findAllBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
