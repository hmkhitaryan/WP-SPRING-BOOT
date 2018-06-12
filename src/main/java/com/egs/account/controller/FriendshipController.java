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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    JsonResponse addFriend(@RequestParam(RECEIVER_USERNAME) String receiverUsername, HttpServletRequest request, HttpServletResponse response) {
        final String initiatorUsername = utilsService.getUserPrincipalName(context);
        if ("".equals(initiatorUsername)) {
            LOGGER.warn("your session is expired, redirecting to login page");
            try {
                final String contextPath = request.getContextPath();
                response.sendRedirect("http://localhost:8001/login");
                //TODO implement proper way of finding context path when the session is expired
                return new JsonResponse("FAILED", "Your session is expired, go to login page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final User initiatorUser = userService.findByUsername(initiatorUsername);
        Optional<Friendship> friendship = friendshipService.findByInitiatorOrReceiver(initiatorUser);

        return doFriendship(receiverUsername, initiatorUser, friendship);
    }

    private JsonResponse doFriendship(@RequestParam(RECEIVER_USERNAME) String receiverUsername, User initiatorUser, Optional<Friendship> friendship) {
        boolean failed = false;
        String message = "";
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
        return userValidator.processValidate(failed, message);
    }

    public static String getUserPrincipalName(HttpServletRequest request) {
        return Optional.of(request.getUserPrincipal().getName())
                .orElseThrow(IllegalStateException::new);
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
}
