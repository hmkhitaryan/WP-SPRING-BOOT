package com.egs.account.service.friendship;

import com.egs.account.model.User;
import com.egs.account.model.chat.Friendship;

import java.util.Optional;

public interface FriendshipService {

    Friendship save(Friendship friendship);

    Friendship findByReceiver(User receiverUser);

    Friendship findByInitiator(User receiverUser);

    Optional<Friendship> findByInitiatorOrReceiver(User user);

    void delete(Friendship friendship);

    Optional<Friendship> findByInitiatorAndReceiver(Long initiatorId, Long receiverId);
}
