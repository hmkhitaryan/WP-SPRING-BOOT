package com.egs.account.repository.friendship;

import com.egs.account.model.chat.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
//    Friendship findByInitiatorOrReceiver(User user);
}
