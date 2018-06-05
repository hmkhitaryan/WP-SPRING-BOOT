package com.egs.account.service.friendship;

import com.egs.account.exception.FriendshipNotFoundException;
import com.egs.account.model.User;
import com.egs.account.model.chat.Friendship;
import com.egs.account.repository.friendship.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    FriendshipRepository friendshipRepository;

    public Friendship save(Friendship friendship) {
        return friendshipRepository.save(friendship);
    }

    @Override
    public Friendship findByReceiver(User receiverUser) {
        return friendshipRepository.findByReceiver(receiverUser);
    }

    @Override
    public Friendship findByInitiator(User initiatorUser) {
        return friendshipRepository.findByInitiator(initiatorUser);
    }

    @Override
    public Friendship findByInitiatorOrReceiver(User user) {
        Friendship friendship = friendshipRepository.findByInitiator(user);
        if (friendship == null) {
            friendship = friendshipRepository.findByReceiver(user);
            if (friendship == null) {
                throw new FriendshipNotFoundException("No friendship found with this user");
            }
        }
        return friendship;
    }

    @Override
    public void delete(Friendship friendship) {
        friendshipRepository.delete(friendship);
    }
}
