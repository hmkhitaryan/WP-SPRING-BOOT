package com.egs.account.controller.interceptor;

import com.egs.account.exception.DocumentNotFoundException;
import com.egs.account.exception.DocumentOutOfBoundException;
import com.egs.account.exception.FriendshipNotFoundException;
import com.egs.account.exception.UserNotFoundException;
import com.egs.account.mapping.UIAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import static com.egs.account.mapping.UIAttribute.*;

/**
 * @author Hayk_Mkhitaryan
 */
@ControllerAdvice
public class UserControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerAdvice.class);

    @Autowired
    MessageSource messageSource;

    /**
     * Default constructor.
     */
    public UserControllerAdvice() {
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException() {
        return initModelAndView("account.user.label", USER_NOT_FOUND);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ModelAndView handleDocumentNotFoundException() {
        return initModelAndView("account.document.label", USER_NOT_FOUND);
    }


    @ExceptionHandler(DocumentOutOfBoundException.class)
    public ModelAndView handleDocumentOutOfBoundException() {
        return initModelAndView("account.document.label", DOC_OUT_OF_BOUND);
    }

    @ExceptionHandler(FriendshipNotFoundException.class)
    public ModelAndView handleFriendshipNotFoundException() {
        return initModelAndView("account.friendship.label", FRIENDSHIP_NOT_FOUND);
    }


    private ModelAndView initModelAndView(String s, String notFound) {
        final ModelAndView modelAndView = new ModelAndView();
        final String domain = messageSource.getMessage(s, null, null);
        modelAndView.addObject(UIAttribute.DOMAIN, domain);
        modelAndView.setViewName(notFound);

        return modelAndView;
    }
}
