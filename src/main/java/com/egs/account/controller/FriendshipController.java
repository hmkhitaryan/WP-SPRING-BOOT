package com.egs.account.controller;

import com.egs.account.mapping.UrlMapping;
import com.egs.account.model.Notification;
import com.egs.account.model.User;
import com.egs.account.model.ajax.JsonResponse;
import com.egs.account.model.chat.Friendship;
import com.egs.account.service.friendship.FriendshipService;
import com.egs.account.service.notification.NotificationService;
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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class FriendshipController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FriendshipController.class);

    private static final String RECEIVER_USERNAME = "receiverUsername";

    private static final String SENDER_USERNAME = "senderUsername";

    private static final String NOTE_ID = "noteId";

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

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = UrlMapping.ADD_FRIEND, method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse sendFriendRequest(@RequestParam(RECEIVER_USERNAME) String receiverUsername, HttpServletResponse response) {
        final String initiatorUsername = utilsService.getUserPrincipalName(context);
        if (expiredSession(response, initiatorUsername)) {
            return new JsonResponse("FAILED", "Your session is expired, go to login page");
        }
        final User initiatorUser = userService.findByUsername(initiatorUsername);
        Optional<Friendship> friendship = friendshipService.findByInitiatorOrReceiver(initiatorUser);
        if (friendship.isPresent()) {
            return new JsonResponse("FAIL", "You are already friends");
        }
        final User receiverUser = userService.findByUsername(receiverUsername);

        return sendFriendshipRequest(initiatorUser, receiverUser);
//        return doFriendship(receiverUsername, initiatorUser, friendship);
    }

    @RequestMapping(value = UrlMapping.CONFIRM_FRIEND, method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse confirmFriendRequest(@RequestParam(SENDER_USERNAME) String senderUsername, @RequestParam(NOTE_ID) Long noteId, HttpServletResponse response) {
        if (expiredSession(response, senderUsername)) {
            return new JsonResponse("FAILED", "Your session is expired, go to login page");
        }
        final User initiatorUser = userService.findByUsername(senderUsername);
        final Optional<Notification> notification = notificationService.findById(noteId);
        if (notification.isPresent()) {
            final Notification note = notification.get();
            note.setSeen(true);
            notificationService.save(note);
            final Friendship friendship = new Friendship(initiatorUser, note.getUser());
            friendshipService.save(friendship);
            return new JsonResponse("OK", "Congratulations!!! you are already friends");
        } else {
            return new JsonResponse("FAIL", "Notification with that id not found");
        }
    }

    private JsonResponse sendFriendshipRequest(User initiatorUser, User receiverUser) {
        final Friendship friendship = new Friendship(initiatorUser, receiverUser);
        friendshipService.save(friendship);
        final Notification notification = new Notification(receiverUser, "Friend request");
        notificationService.save(notification);

        return new JsonResponse("OK", "Your request for adding friend is sent");
    }

    private boolean expiredSession(HttpServletResponse response, String initiatorUsername) {
        if ("".equals(initiatorUsername)) {
            LOGGER.warn("your session is expired, redirecting to login page");
            try {
                final String contextPath = servletContext.getContextPath();
                response.sendRedirect(contextPath + "/login");
                //TODO implement proper way of finding context path when the session is expired
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
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
