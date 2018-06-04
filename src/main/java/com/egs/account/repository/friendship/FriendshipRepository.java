package com.egs.account.repository.friendship;

import com.egs.account.model.User;
import com.egs.account.model.chat.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Friendship findByInitiator(User initiator);

    Friendship findByReceiver(User receiver);
}
