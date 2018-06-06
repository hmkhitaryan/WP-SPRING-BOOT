package com.egs.account.controller;

import com.egs.account.mapping.UrlMapping;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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

    @RequestMapping(value = UrlMapping.ADD_FRIEND, method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse addFriend(@RequestParam(RECEIVER_USERNAME) String receiverUsername) {
        final String initiatorUsername = context.getUserPrincipal().getName();
        boolean failed = false;
        final User initiatorUser = userService.findByUsername(initiatorUsername);
        Optional<Friendship> friendship = friendshipService.findByInitiatorOrReceiver(initiatorUser);

        String message = "";
        processIfFailed(friendship, message, receiverUsername, failed, initiatorUser);

        return userValidator.processValidate(failed, message);
    }

    @RequestMapping(value = UrlMapping.UN_FRIEND, method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse unFriend(@RequestParam(RECEIVER_USERNAME) String receiverUsername) {
        boolean failed = false;
        final User receiverUser = userService.findByUsername(receiverUsername);
        String message = "";
        if (receiverUser == null) {
            failed = true;
            message = "Requested user not found";
        }
        final Friendship friendship = friendshipService.findByReceiver(receiverUser);
        friendshipService.delete(friendship);

        return userValidator.processValidate(failed, message);
    }

    private void processIfFailed(Optional<Friendship> friendship, String message, String receiverUsername, boolean failed, User initiatorUser) {
        if (!friendship.isPresent()) {
            final User receiverUser = userService.findByUsername(receiverUsername);
            if (receiverUser == null) {
                failed = true;
                message = "Requested user not found";
            }
            friendship = Optional.of(new Friendship(initiatorUser, receiverUser));
            friendshipService.save(friendship.get());
            if (friendship.get().getId() == null) {
                failed = true;
            }
        }
    }
}
