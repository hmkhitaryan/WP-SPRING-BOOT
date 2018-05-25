package com.egs.account.utils.domainUtils;

import com.egs.account.exception.DocumentNotFoundException;
import com.egs.account.exception.DocumentOutOfBoundException;
import com.egs.account.exception.UserNotFoundException;
import com.egs.account.mapping.UIAttribute;
import com.egs.account.mapping.UrlMapping;
import com.egs.account.model.Catalog;
import com.egs.account.model.FileBucket;
import com.egs.account.model.User;
import com.egs.account.service.catalog.CatalogService;
import com.egs.account.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DomainUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainUtils.class);

    private static final int maxUploadFileSize = 1000000;

    private static final String SLASH_SIGN = "/";

    private static final String DOT_SIGH = ".";

    private static final String TEMP = "temp/";

    private static final String FILE_FULL_PATH = "C:\\Users\\haykmk\\hmkhitaryan\\dev\\proj\\temp\\";

    private static final String JPEG_TYPE = "image/jpeg";

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private UserService userService;

    private void saveDocument(FileBucket fileBucket, User user) {
        final MultipartFile multipartFile = fileBucket.getFile();
        final String originalFileName = multipartFile.getOriginalFilename();
        final String fileName = user.getId() + DOT_SIGH + originalFileName;
        validateFile(multipartFile);

        try {
            final byte[] bytes = multipartFile.getBytes();
            final Path path = Paths.get(FILE_FULL_PATH + fileName);
            Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        final Catalog document = getCatalog(fileBucket, user, multipartFile, fileName);
        catalogService.saveDocument(document);
    }

    private Catalog getCatalog(FileBucket fileBucket, User user, MultipartFile multipartFile, String fileName) {
        final Catalog document = new Catalog();
        document.setLink(TEMP + fileName);
        document.setFullFileName(FILE_FULL_PATH + fileName);
        document.setComment(fileBucket.getComment());
        document.setType(multipartFile.getContentType());
        document.setInsertDate(new Date());
        document.setUser(user);

        return document;
    }

    private void validateFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new RuntimeException("Please load a file");
        }
        if (multipartFile.getSize() > maxUploadFileSize) {
            throw new DocumentOutOfBoundException("document size is out of its bound");
        }
    }

    private byte[] getUserFileByDocId(Long docId) {
        final String fullFileName = catalogService.findById(docId).getFullFileName();
        byte[] fileBytes = null;
        try {
            final File file = new File(fullFileName);
            fileBytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            LOGGER.error("something went wrong with getting file");
        }

        return fileBytes;
    }

    public List<String> getImageLinks(List<Catalog> catalogs) {
        final List<String> links = new ArrayList<>();
        for (Catalog catalog : catalogs) {
            if (catalog.getType().equalsIgnoreCase(JPEG_TYPE)) {
                links.add(catalog.getLink());
            }
        }

        return links;
    }

    public void downloadDocument(HttpServletResponse response, Long docId) {
        final byte[] fileContent = getUserFileByDocId(docId);
        if (fileContent == null) {
            return;
        }
        final Catalog document = catalogService.findById(docId);
        handleNotFoundError(document, Catalog.class, docId);
        processDownload(response, fileContent, document);
    }

    private void processDownload(HttpServletResponse response, byte[] fileContent, Catalog document) {
        response.setContentType(document.getType());
        response.setContentLength(fileContent.length);
        final String value = "attachment; filename=\"" + document.getLink() + "\"";
        response.setHeader("Content-Disposition", value);

        try {
            FileCopyUtils.copy(fileContent, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("something went wrong with downloading file");
        }
    }

    public boolean isLoggedInUser(HttpServletRequest context, User user) {
        if (context.getUserPrincipal() == null) {
            return false;
        }
        return user.getUsername() != null && user.getUsername().equals(context.getUserPrincipal().getName());
    }

    public String uploadDocument(FileBucket fileBucket, BindingResult result, ModelMap model, Long userId) {
        final User user = userService.findById(userId);
        handleNotFoundError(user, User.class, userId);
        if (result.hasErrors()) {
            LOGGER.error("validation errors");
            model.addAttribute(UIAttribute.USER, user);
            final List<Catalog> documents = catalogService.findAllByUserId(userId);
            model.addAttribute(UIAttribute.DOCUMENTS, documents);

            return UrlMapping.MANAGE_DOC_VIEW;
        } else {
            LOGGER.info("Fetching file");
            model.addAttribute(UIAttribute.USER, user);
            saveDocument(fileBucket, user);

            return UrlMapping.ADD_DOC_REDIRECT_VIEW + SLASH_SIGN + userId;
        }
    }

    public void handleNotFoundError(Object entity, Class clazz, Object id) {
        if (entity == null) {
            if (clazz.equals(User.class)) {
                throw new UserNotFoundException(String.format("No user found with this identifier %s", id));
            }
            if (clazz.equals(Catalog.class)) {
                throw new DocumentNotFoundException(String.format("No document found with this id %s", id));
            }
        }
    }
}
