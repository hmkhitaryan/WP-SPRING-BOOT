package com.egs.account.service.notification;

import com.egs.account.model.chat.Notification;
import com.egs.account.repository.notification.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by haykmk on 6/21/2018.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Override
    public Notification save(Notification notification) {
        return notificationRepo.save(notification);
    }

    @Override
    public List<Notification> findAllByUserId(Long userId) {
        return notificationRepo.findAllByUserId(userId);
    }

    @Override
    public List<Notification> findAllByUserIdNotSeen(Long userId) {
        return notificationRepo.findAllByUserIdNotSeen(userId);
    }

    @Override
    public Optional<Notification> findById(Long noteId) {
        return notificationRepo.findById(noteId);
    }

    @Override
    public void delete(Long noteId) {
        notificationRepo.deleteById(noteId);
    }
}
