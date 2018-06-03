package com.egs.account.controller;

import com.egs.account.model.User;
import com.egs.account.model.ajax.JsonResponse;
import com.egs.account.model.chat.Friendship;
import com.egs.account.service.friendship.FriendshipService;
import com.egs.account.service.user.UserService;
import com.egs.account.service.validator.ValidationService;
import com.egs.account.utils.domainUtils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FriendshipController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FriendshipController.class);
    private static final String RECEIVER_USERNAME = "receiverUsername";

    @Autowired
    private UserService userService;

    @Autowired
    private DomainUtils utilsService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private ValidationService userValidator;

    @RequestMapping(value = "/addFriend", method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse addFriend(@RequestParam(RECEIVER_USERNAME) String receiverUsername) {
        final String initiatorUsername = context.getUserPrincipal().getName();
        boolean failed = false;
        final User initiatorUser = userService.findByUsername(initiatorUsername);
        final User receiverUser = userService.findByUsername(receiverUsername);
        if (receiverUser == null) {
            failed = true;
        }
        final Friendship friendship = new Friendship(initiatorUser, receiverUser);
        friendshipService.save(friendship);
        if (friendship.getId() == null) {
            failed = true;
        }

        return userValidator.processValidate(failed);
    }
}
