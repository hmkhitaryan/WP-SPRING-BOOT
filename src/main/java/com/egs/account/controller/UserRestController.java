package com.egs.account.controller;

import com.egs.account.model.User;
import com.egs.account.service.user.UserService;
import com.egs.account.utils.convertor.JsonConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserRestController {

    @Autowired
    UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    //-------------------Retrieve All Users--------------------------------------------------------

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String listAllUsers() {
        String userAsJson = "";
		final List<User> users = userService.findAllUsers();
		try {
            userAsJson = JsonConvertor.toJson(users);
        } catch (Exception ex) {
            LOGGER.error("Something went wrong during converting json format");
        }
        LOGGER.info("displaying all users in json format");
        return userAsJson;

    }


    //-------------------Retrieve Single User--------------------------------------------------------

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getUser(@PathVariable("id") Long id) {
        String userAsJson = "";
		final User user = userService.findById(id);
		if (user == null) {
            LOGGER.info("no user found with id {}", id);
            return null;
        }
        try {
            userAsJson = JsonConvertor.toJson(user);
        } catch (Exception ex) {
            LOGGER.error("Something went wrong during converting json format");
        }
        LOGGER.info("getting the user {} in json format", id);
        return userAsJson;
    }

}