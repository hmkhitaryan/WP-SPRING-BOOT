package com.egs.account.controller;

import com.egs.account.model.User;
import com.egs.account.model.chat.Friendship;
import com.egs.account.service.friendship.FriendshipService;
import com.egs.account.service.user.UserService;
import com.egs.account.utils.domainUtils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FriendshipController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FriendshipController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private DomainUtils utilsService;

    @Autowired
    private FriendshipService friendshipService;

    @RequestMapping(value = "/addFriend", method = RequestMethod.POST)
    public String addFriend(@PathVariable Long userInitId, @PathVariable Long userReceiverId) {
        final User initiatorUser = userService.findById(userInitId);
        utilsService.handleNotFoundError(initiatorUser, User.class, userInitId);
        final User receiverUser = userService.findById(userReceiverId);
        utilsService.handleNotFoundError(receiverUser, User.class, userReceiverId);
        final Friendship friendship = new Friendship(initiatorUser, receiverUser);
        friendshipService.save(friendship);

        return "successFriendship";
    }
}
