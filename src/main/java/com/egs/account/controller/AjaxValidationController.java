package com.egs.account.controller;

import com.egs.account.mapping.UrlMapping;
import com.egs.account.model.ajax.JsonResponse;
import com.egs.account.service.validator.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Hayk_Mkhitaryan
 */
@Controller
public class AjaxValidationController {

    private static final String PASSWORD = "password";

    private static final String PASSWORD_CONFIRM = "passwordConfirm";

    private static final String EMAIL = "email";

    private static final String USERNAME = "username";

    @Autowired
    private ValidationService userValidator;

    @RequestMapping(value = UrlMapping.CHECK_USERNAME, method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse resolveUsernameValidation(@RequestParam(USERNAME) String username) {
        return doValidation(userValidator.isInvalidUsername(username), "Invalid username");
    }

    @RequestMapping(path = {UrlMapping.CHECK_EMAIL, UrlMapping.CHECK_EMAIL_EDIT}, method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse resolveEmailValidation(@RequestParam(EMAIL) String email) {
        return doValidation(userValidator.isInvalidUsername(email), "Invalid email");
    }

    @RequestMapping(path = {UrlMapping.CHECK_PASSWORD, UrlMapping.CHECK_PASSWORD_EDIT}, method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse resolvePasswordValidation(@RequestParam(PASSWORD) String password) {
        return doValidation(userValidator.isInvalidUsername(password), "Invalid password");
    }

    @RequestMapping(value = UrlMapping.CHECK_PASSWORD_CONFIRM, method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse resolvePasswordConfirmValidation(@RequestParam(PASSWORD) String password, @RequestParam(PASSWORD_CONFIRM) String passwordConfirm) {
        return doValidation(!userValidator.passwordsMatch(password, passwordConfirm), "Passwords do not match");
    }

    private JsonResponse doValidation(boolean negativeExp, String message) {
        String errorMessage = "";
        boolean failed = false;
        if (negativeExp) {
            errorMessage = message;
            failed = true;
        }
        return userValidator.processValidate(failed, message);
    }
}
