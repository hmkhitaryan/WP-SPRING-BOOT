package com.egs.account.repository.friendship;

import com.egs.account.model.User;
import com.egs.account.model.chat.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Friendship findByInitiator(User initiator);

    Friendship findByReceiver(User receiver);

    @Query("SELECT f FROM Friendship f join fetch f.initiator  join fetch f.receiver WHERE f.initiator.id = :userId OR f.receiver.id = :userId")
    Friendship findByInitiatorOrReceiver(@Param("userId") Long userId);

    @Query("SELECT f FROM Friendship f join fetch f.initiator  join fetch f.receiver WHERE " +
            "(f.initiator.id = :initiatorId AND f.receiver.id = :receiverId) OR (f.receiver.id = :initiatorId AND f.initiator.id = :receiverId)")
    Friendship findByInitiatorAndReceiver(@Param("initiatorId") Long initiatorId, @Param("receiverId") Long receiverId);
}
