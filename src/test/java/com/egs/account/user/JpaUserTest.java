package com.egs.account.user;

import com.egs.account.model.Notification;
import com.egs.account.model.User;
import com.egs.account.repository.notification.NotificationRepo;
import com.egs.account.repository.user.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by haykmk on 6/22/2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaUserTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepo notificationRepo;

    @Test
    public void notificationFetchTest() {   // also covers user save with notification, notification find
        final User user = constructUser();
        final Notification notification1 = new Notification(user, "For friend request1", true);
        final Notification notification2 = new Notification(user, "For friend request2", true);
        final Notification notification3 = new Notification(user, "For friend request3", true);
        final Notification notificationNotSeen = new Notification(user, "For friend request", false);
        notificationNotSeen.setId(8L);
        Collections.addAll(user.getNotifications(), notification1, notification2, notification3, notificationNotSeen);

        final User savedUser = userRepository.save(user);
        Assert.assertNotNull(savedUser.getId());
        final List<Notification> notificationList = notificationRepo.findAllByUserIdNotSeen(savedUser.getId());

        Assert.assertTrue(!notificationList.contains(notificationNotSeen));
        System.out.println();
    }

    private User constructUser() {
        final User user = new User();
        user.setId(1L);
        user.setUsername("hmkhitaryan");
        user.setFirstName("Hayk");
        user.setLastName("Mkhitaryan");
        user.setEmail("hayk_84@mail.ru");
        user.setNotifications(new HashSet<>(4));

        return user;
    }
}
