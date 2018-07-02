package com.egs.account.repository.notification;

import com.egs.account.model.chat.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Hayk_Mkhitaryan
 */
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n join fetch n.initiator  join fetch n.receiver WHERE n.receiver.id = :userId")
    List<Notification> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT n FROM Notification n join fetch n.initiator  join fetch n.receiver WHERE (n.receiver.id = :userId and n.seen = false )")
    List<Notification> findAllByUserIdNotSeen(@Param("userId") Long userId);
}
