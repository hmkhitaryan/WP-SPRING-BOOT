package com.egs.account.controller;

import com.egs.account.event.OnRegistrationCompleteEvent;
import com.egs.account.exception.UserNotFoundException;
import com.egs.account.mapping.UIAttribute;
import com.egs.account.mapping.UrlMapping;
import com.egs.account.model.Catalog;
import com.egs.account.model.User;
import com.egs.account.model.ajax.JsonUser;
import com.egs.account.service.catalog.CatalogService;
import com.egs.account.service.security.SecurityService;
import com.egs.account.service.user.UserService;
import com.egs.account.service.validator.ValidationService;
import com.egs.account.utils.convertor.UserToJsonUserConverter;
import com.egs.account.utils.domainUtils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

/**
 * @author Hayk_Mkhitaryan
 */

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final String MAP = "map";

    private static final String USER = "user";

    private static final String UPDATED_SUCCESSFULLY = " updated successfully";

    private static final String DOC_SIZE = "docSize";

    private static final String BUCKET_LINKS = "bucketLinks";

    private static final String HTTP = "http://";

    private static final String COLON_SIGN = ":";

    private static final String TOKEN_INVALID = "invalidToken";

    private static final String TOKEN_EXPIRED = "expired";

    @Autowired
    MessageSource messageSource;

    @Autowired
    DomainUtils utilsService;

    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ValidationService userValidator;

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = UrlMapping.LOGIN, method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            final String loginError = messageSource.getMessage("userName.password.error", null, null);
            model.addAttribute(UIAttribute.ERROR, loginError);
        }

        if (logout != null) {
            final String logoutError = messageSource.getMessage("logout.error", null, null);
            model.addAttribute(UIAttribute.MESSAGE, logoutError);
        }

        return UrlMapping.LOGIN_VIEW;
    }

    @RequestMapping(value = UrlMapping.TO_MAP, method = RequestMethod.GET)
    public ModelAndView getPages() {
        return new ModelAndView(MAP);
    }

    @RequestMapping(value = UrlMapping.WELCOME, method = RequestMethod.GET)
    public String welcome(Model model) {
        final String username = context.getUserPrincipal().getName();
        final User userForm = userService.findByUsername(username);
        model.addAttribute(UIAttribute.USER_FORM, userForm);
        final List<Catalog> catalogs = catalogService.findAllByUserId(userForm.getId());
        final List<String> links = utilsService.getImageLinks(catalogs);
        model.addAttribute(BUCKET_LINKS, links);
        model.addAttribute(DOC_SIZE, links.size());

        return UrlMapping.WELCOME_VIEW;
    }

    @RequestMapping(value = UrlMapping.REGISTRATION, method = RequestMethod.GET)
    public String showRegistrationPage(Model model) {
        model.addAttribute(UIAttribute.USER_FORM, new User());

        return UrlMapping.REGISTRATION_VIEW;
    }

    @RequestMapping(value = UrlMapping.REGISTRATION, method = RequestMethod.POST)
    public String processRegistrationPage(@ModelAttribute(UIAttribute.USER_FORM) User userForm, BindingResult bindingResult,
                                          final HttpServletRequest request, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return UrlMapping.REGISTRATION_VIEW;
        }
        User registered = userService.saveUser(userForm);
        if (registered == null) {
            return UrlMapping.REGISTRATION_VIEW;
        }

        try {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        } catch (Exception ex) {
            LOGGER.warn("Unable to register user", ex);

            return UrlMapping.EMAIL_ERROR_VIEW;
        }
        model.addAttribute("user", userForm);

        return UrlMapping.SUCCESS_REGISTER_VIEW;
    }

    @RequestMapping(value = UrlMapping.REGISTRATION_CONFIRM, method = RequestMethod.GET)
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
        final Locale locale = request.getLocale();
        final String validatedToken = userService.validateVerificationToken(token);
        if (invalidToken(model, locale, validatedToken)) {
            return UrlMapping.BAD_USER_REDIRECT + locale.getLanguage();
        }

        return UrlMapping.LOGIN_REDIRECT + locale.getLanguage();
    }

    @RequestMapping(value = {UrlMapping.EDIT_USER + "/{id}"}, method = RequestMethod.GET)
    public String showUpdateUserPage(@PathVariable Long id, ModelMap model) {
        final User user = userService.findById(id);
        if ((user == null || context.getUserPrincipal() == null) ||
                (user.getUsername() != null && !user.getUsername().equals(context.getUserPrincipal().getName()))) {

            return UrlMapping.LOGIN_VIEW;
        }
        model.addAttribute(USER, user);

        return UrlMapping.EDIT_USER_VIEW;
    }

    @RequestMapping(value = {UrlMapping.EDIT_USER + "/{id}"}, method = RequestMethod.POST)
    public String processUpdateUserPage(@ModelAttribute User user, BindingResult bindingResult, ModelMap model,
                                        @PathVariable Long id) {
        user.setUpdated(true);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return UrlMapping.EDIT_USER_VIEW;
        }
        userService.updateUser(user);
        model.addAttribute(UIAttribute.USER_FORM, user);
        model.addAttribute(UIAttribute.SUCCESS, "User " + user.getFirstName() + " " + user.getLastName() +
                UPDATED_SUCCESSFULLY);

        return UrlMapping.REGISTRATION_SUCCESS_VIEW;
    }

    @RequestMapping(value = {UrlMapping.DELETE_USER + "/{id}"}, method = RequestMethod.GET)
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);

        return UrlMapping.DELETE_SUCCESS_VIEW;
    }

    @RequestMapping(value = "/findUser", method = RequestMethod.POST)
    public @ResponseBody
    JsonUser findUserByUsername(@RequestParam("username") String username) {
        User userFound = userService.findByUsername(username);
        if (userFound == null) {
            throw new UserNotFoundException("no user found with specified username");
        }
        return UserToJsonUserConverter.toJsonUser(userFound);
    }

    private String getAppUrl(HttpServletRequest request) {
        return HTTP + request.getServerName() + COLON_SIGN + request.getServerPort() + request.getContextPath();
    }


    private boolean invalidToken(Model model, Locale locale, String validatedToken) {
        if (validatedToken.equalsIgnoreCase(TOKEN_INVALID)) {
            final String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);

            return true;
        }
        if (validatedToken.equalsIgnoreCase(TOKEN_EXPIRED)) {
            final String messageValue = messageSource.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);

            return true;
        }
        return false;
    }
}