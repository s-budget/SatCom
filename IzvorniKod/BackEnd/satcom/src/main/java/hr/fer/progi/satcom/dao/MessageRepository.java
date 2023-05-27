package hr.fer.progi.satcom.dao;

import hr.fer.progi.satcom.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE from messages where messages.user_id = ?1", nativeQuery = true)
    void deleteAllMessages(Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE from messages where messages.message_id = ?1", nativeQuery = true)
    void deleteMessage(Long messageId);
}
