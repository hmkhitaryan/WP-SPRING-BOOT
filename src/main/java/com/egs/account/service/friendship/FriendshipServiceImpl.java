package com.egs.account.service.friendship;

import com.egs.account.model.User;
import com.egs.account.model.chat.Friendship;
import com.egs.account.repository.friendship.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    FriendshipRepository friendshipRepository;

    public Friendship save(Friendship friendship) {
        final User initiatorUser = friendship.getInitiator();
        final User receiverUser = friendship.getReceiver();
        final Optional<Friendship> frAlreadySaved = Optional.ofNullable(
                friendshipRepository.findByInitiatorAndReceiver(initiatorUser.getId(), receiverUser.getId()));

        if (!frAlreadySaved.isPresent()) {
            final Friendship friendshipToSave = new Friendship(initiatorUser, receiverUser);
            return friendshipRepository.save(friendshipToSave);
        }
        return frAlreadySaved.get();
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
    public Optional<Friendship> findByInitiatorOrReceiver(User user) {
        return Optional.ofNullable(friendshipRepository.findByInitiatorOrReceiver(user.getId()));
    }

    @Override
    public void delete(Friendship friendship) {
        friendshipRepository.delete(friendship);
    }

    @Override
    public Optional<Friendship> findByInitiatorAndReceiver(Long initiatorId, Long receiverId) {
        return Optional.ofNullable(friendshipRepository.findByInitiatorAndReceiver(initiatorId, receiverId));
    }
}
