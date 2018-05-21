package com.egs.account.controller;

import com.egs.account.mapping.UIAttribute;
import com.egs.account.mapping.UrlMapping;
import com.egs.account.model.Catalog;
import com.egs.account.model.FileBucket;
import com.egs.account.model.User;
import com.egs.account.service.catalog.CatalogService;
import com.egs.account.service.user.UserService;
import com.egs.account.utils.domainUtils.DomainUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Hayk_Mkhitaryan
 */

@Controller
public class UploadController {
    private static final String SLASH_SIGN = "/";

    @Autowired
    DomainUtils domainUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private HttpServletRequest context;

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
    public String uploadDocument(ModelMap model, @ModelAttribute FileBucket fileBucket, BindingResult result,
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
