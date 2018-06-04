package com.egs.account.service.friendship;

import com.egs.account.model.User;
import com.egs.account.model.chat.Friendship;

public interface FriendshipService {
    Friendship save(Friendship friendship);

    Friendship findByReceiver(User receiverUser);

    Friendship findByInitiator(User receiverUser);

    void delete(Friendship friendship);
}
