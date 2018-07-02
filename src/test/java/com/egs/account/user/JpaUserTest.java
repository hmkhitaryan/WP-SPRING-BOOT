package com.egs.account.user;

import com.egs.account.model.User;
import com.egs.account.model.chat.Friendship;
import com.egs.account.model.chat.Notification;
import com.egs.account.repository.friendship.FriendshipRepository;
import com.egs.account.repository.notification.NotificationRepo;
import com.egs.account.repository.user.UserRepository;
import com.egs.account.service.friendship.FriendshipService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private FriendshipService friendshipService;

    @Test
    public void notificationFetchTest() {   // also covers user save with notification, notification find
        final User user1 = constructUser("hmkhitaryan1", "hayk1_84@mail.ru");
        final User user2 = constructUser("hmkhitaryan2", "hayk2_84@mail.ru");
        final User savedUser1 = userRepository.save(user1);
        final User savedUser2 = userRepository.save(user2);
        final Notification notification1 = new Notification(user1, user2, true);
        final Notification notification2 = new Notification(user1, user2, true);
        final Notification notification3 = new Notification(user1, user2, true);
        final Notification notificationNotSeen1 = new Notification(user1, user2, false);
        final Notification notificationNotSeen2 = new Notification(user1, user2, false);

        notificationRepo.save(notification1);
        notificationRepo.save(notification2);
        notificationRepo.save(notification3);
        notificationRepo.save(notificationNotSeen1);
        notificationRepo.save(notificationNotSeen2);

        final List<Notification> notificationList = notificationRepo.findAllByUserIdNotSeen(savedUser2.getId());

        Assert.assertTrue(!notificationList.contains(notification1));
        System.out.println();
    }

    @Test
    public void friendshipFetchTest() {
        final User user1 = constructUser("hmkhitaryan1", "hayk1_84@mail.ru");
        final User user2 = constructUser("hmkhitaryan2", "hayk2_84@mail.ru");
        final Friendship friendship = new Friendship(user1, user2);
        final User savedUser1 = userRepository.save(user1);
        final User savedUser2 = userRepository.save(user2);

        final Friendship savedFriendship = friendshipService.save(friendship);
        final Friendship foundFriendshipOr = friendshipService.findByInitiatorOrReceiver(user1).get();
        Assert.assertEquals(foundFriendshipOr.getInitiator(), user1);

        final Friendship frToSave1 = new Friendship(savedUser1, savedUser2);
        final Friendship savedFr1 = friendshipService.save(frToSave1);
        final Friendship frToSave2 = new Friendship(savedUser1, savedUser2);
        final Friendship savedFr2 = friendshipService.save(frToSave2);
        Assert.assertEquals(savedFr1.getId(), savedFr2.getId());

        System.out.println();
    }

    private User constructUser(String username, String email) {
        final User user = new User();
        user.setUsername(username);
        user.setFirstName("Hayk");
        user.setLastName("Mkhitaryan");
        user.setEmail(email);

        return user;
    }
}
