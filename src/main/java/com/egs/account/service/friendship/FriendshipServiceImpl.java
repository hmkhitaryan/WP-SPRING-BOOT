package com.egs.account.service.friendship;

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
}
