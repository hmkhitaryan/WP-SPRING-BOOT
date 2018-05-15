package com.egs.account.controller.interceptor;

import com.egs.account.exception.DocumentNotFoundException;
import com.egs.account.exception.UserNotFoundException;
import com.egs.account.mapping.UIAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import static com.egs.account.mapping.UIAttribute.NOT_FOUND;

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
    public ModelAndView handleUserError() {
        final ModelAndView modelAndView = new ModelAndView();
        final String domain = messageSource.getMessage("account.user.label", null, null);
        modelAndView.addObject(UIAttribute.DOMAIN, domain);
        modelAndView.setViewName(NOT_FOUND);

        return modelAndView;
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ModelAndView handleDocumentError() {
        final ModelAndView modelAndView = new ModelAndView();
        final String domain = messageSource.getMessage("account.document.label", null, null);
        modelAndView.addObject(UIAttribute.DOMAIN, domain);
        modelAndView.setViewName(NOT_FOUND);

        return modelAndView;
    }
}
