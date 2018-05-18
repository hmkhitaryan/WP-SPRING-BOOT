package com.egs.account.controller;

import com.egs.account.mapping.UIAttribute;
import com.egs.account.mapping.UrlMapping;
import com.egs.account.model.Catalog;
import com.egs.account.model.FileBucket;
import com.egs.account.model.User;
import com.egs.account.service.catalog.CatalogService;
import com.egs.account.service.security.SecurityService;
import com.egs.account.service.user.UserService;
import com.egs.account.service.validator.ValidationService;
import com.egs.account.utils.domainUtils.DomainUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


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
    private CatalogService catalogService;

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

    @InitBinder("userForm")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
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
    public String createUser(Model model) {
        model.addAttribute(UIAttribute.USER_FORM, new User());

        return UrlMapping.REGISTRATION_VIEW;
    }

    @RequestMapping(value = UrlMapping.REGISTRATION, method = RequestMethod.POST)
    public String createUser(@ModelAttribute(UIAttribute.USER_FORM) User userForm, BindingResult bindingResult) {
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
    public String updateUser(@PathVariable Long id, ModelMap model) {
        final User user = userService.findById(id);
        if (user.getUsername() != null && !user.getUsername().equals(context.getUserPrincipal().getName())) {
            return UrlMapping.LOGIN_VIEW;
        }

        model.addAttribute(UIAttribute.USER_FORM, user);

        return UrlMapping.EDIT_USER_VIEW;
    }

    @RequestMapping(value = {UrlMapping.EDIT_USER + "/{id}"}, method = RequestMethod.POST)
    public String updateUser(@ModelAttribute User userForm, BindingResult bindingResult, ModelMap model,
                             @PathVariable Long id) {
        userValidator.validateEdit(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return UrlMapping.EDIT_USER_REDIRECT_VIEW + "/" + id;
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

    @RequestMapping(value = {UrlMapping.ADD_DOCUMENT + "/{id}"}, method = RequestMethod.GET)
    public String uploadDocument(@PathVariable Long id, ModelMap model) {
        final User user = userService.findById(id);
        boolean isLoggedInUser = domainUtils.isLoggedInUser(context, user);
        if (!isLoggedInUser) {
            return UrlMapping.LOGIN_VIEW;
        }

        model.addAttribute(UIAttribute.USER, user);
        final FileBucket fileModel = new FileBucket();
        model.addAttribute(UIAttribute.FILE_BUCKET, fileModel);
        final List<Catalog> documents = catalogService.findAllByUserId(id);
        model.addAttribute(UIAttribute.DOCUMENTS, documents);

        return UrlMapping.MANAGE_DOC_VIEW;
    }

    @RequestMapping(value = {UrlMapping.ADD_DOCUMENT + "/{userId}"}, method = RequestMethod.POST)
    public String uploadDocument(@ModelAttribute FileBucket fileBucket, BindingResult result, ModelMap model,
                                 @PathVariable Long userId) throws IOException {

        return domainUtils.uploadDocument(fileBucket, result, model, userId);
    }

    @RequestMapping(value = {UrlMapping.DOWNLOAD_DOCUMENT + "/{userId}/{docId}"}, method = RequestMethod.GET)
    public String downloadDocument(@PathVariable Long userId, @PathVariable Long docId, HttpServletResponse response)
            throws IOException {
        domainUtils.downloadDocument(response, docId);

        return UrlMapping.ADD_DOC_REDIRECT_VIEW + SLASH_SIGN + userId;
    }

    @RequestMapping(value = {UrlMapping.DELETE_DOCUMENT + "/{userId}/{docId}"}, method = RequestMethod.GET)
    public String deleteDocument(@PathVariable Long userId, @PathVariable Long docId) {
        catalogService.deleteById(docId);

        return UrlMapping.ADD_DOC_REDIRECT_VIEW + SLASH_SIGN + userId;
    }
}