package com.egs.account.controller;

import com.egs.account.mapping.UIAttribute;
import com.egs.account.mapping.UrlMapping;
import com.egs.account.model.User;
import com.egs.account.service.security.SecurityService;
import com.egs.account.service.user.UserService;
import com.egs.account.service.validator.ValidationService;
import com.egs.account.utils.domainUtils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Hayk_Mkhitaryan
 */

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final String SLASH_SIGN = "/";

    private static final String MAP = "map";

    @Autowired
    MessageSource messageSource;

    @Autowired
    DomainUtils domainUtils;

    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ValidationService userValidator;

    @Autowired
    private HttpServletRequest context;

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
        final String userName = context.getUserPrincipal().getName();
        final User userForm = userService.findByUsername(userName);
        model.addAttribute(UIAttribute.USER_FORM, userForm);

        return UrlMapping.WELCOME_VIEW;
    }

    @RequestMapping(value = UrlMapping.REGISTRATION, method = RequestMethod.GET)
    public String showRegistrationPage(Model model) {
        model.addAttribute(UIAttribute.USER_FORM, new User());

        return UrlMapping.REGISTRATION_VIEW;
    }

    @RequestMapping(value = UrlMapping.REGISTRATION, method = RequestMethod.POST)
    public String processRegistrationPage(@ModelAttribute(UIAttribute.USER_FORM) User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return UrlMapping.REGISTRATION_VIEW;
        }
        userService.saveUser(userForm);
        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        LOGGER.info("user with username {} successfully registered", userForm.getUsername());

        return UrlMapping.WELCOME_REDIRECT_VIEW;
    }

    @RequestMapping(value = {UrlMapping.EDIT_USER + "/{id}"}, method = RequestMethod.GET)
    public String showUpdateUserPage(@PathVariable Long id, ModelMap model) {
        final User user = userService.findById(id);
        if ((user == null || context.getUserPrincipal() == null) ||
                (user.getUsername() != null && !user.getUsername().equals(context.getUserPrincipal().getName()))) {

            return UrlMapping.LOGIN_VIEW;
        }

        model.addAttribute(UIAttribute.USER_FORM, user);

        return UrlMapping.EDIT_USER_VIEW;
    }

    @RequestMapping(value = {UrlMapping.EDIT_USER + "/{id}"}, method = RequestMethod.POST)
    public String processUpdateUserPage(@ModelAttribute User userForm, BindingResult bindingResult, ModelMap model,
                             @PathVariable Long id) {
        userForm.setUpdated(true);
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return UrlMapping.EDIT_USER_REDIRECT_VIEW + SLASH_SIGN + id;
        }
        userService.updateUser(userForm);
        model.addAttribute(UIAttribute.USER_FORM, userForm);
        model.addAttribute(UIAttribute.SUCCESS, "User " + userForm.getFirstName() + " " + userForm.getLastName() +
                " updated successfully");

        return UrlMapping.REGISTRATION_SUCCESS_VIEW;
    }

    @RequestMapping(value = {UrlMapping.DELETE_USER + "/{id}"}, method = RequestMethod.GET)
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);

        return UrlMapping.DELETE_SUCCESS_VIEW;
    }
}