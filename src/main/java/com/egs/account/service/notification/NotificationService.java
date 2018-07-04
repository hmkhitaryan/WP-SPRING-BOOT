package com.egs.account.service.notification;

import com.egs.account.model.chat.Notification;

import java.util.List;
import java.util.Optional;

/**
 * @author Hayk_Mkhitaryan
 */
public interface NotificationService {
    Notification save(Notification notification);

    List<Notification> findAllByUserId(Long userId);

    List<Notification> findAllByUserIdNotSeen(Long userId);

    Optional<Notification> findById(Long noteId);

    void delete(Long noteId);

    void notify(Notification notification, String username);
}
