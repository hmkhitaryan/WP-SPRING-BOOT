package com.egs.account.service.validator;

import com.egs.account.model.User;
import com.egs.account.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class UserValidationService implements Validator {

    private static final String NOT_EMPTY = "NotEmpty";

    private static final String USERNAME = "username";

    private static final String PASSWORD_CONFIRM = "passwordConfirm";

    private static final String EMAIL = "email";

    private static final String AT = "@";

    private static final String PASSWORD = "password";

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        final User user = (User) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, USERNAME, NOT_EMPTY);

        if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
            errors.rejectValue(USERNAME, "Size.userForm.username");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue(USERNAME, "Duplicate.userForm.username");
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue(PASSWORD_CONFIRM, "Diff.userForm.passwordConfirm");
        }

        validatePassword(errors, user.getPassword());

        if (isInvalidEmail(user.getEmail())) {
            errors.rejectValue(EMAIL, "invalid.email");
        }
    }

    public boolean isInvalidEmail(String email) {
        return !email.contains(AT) || !email.contains(".");
    }

    private void validatePassword(Errors errors, String password) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, PASSWORD, "NotEmpty");
        if (isInvalidPassword(password)) {
            errors.rejectValue(PASSWORD, "Size.userForm.password");
        }
    }

    public boolean isInvalidPassword(String password) {
        return password.length() < 8 || password.length() > 32;
    }

    public void validateEdit(Object o, Errors errors) {
        final User user = (User) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, PASSWORD, "NotEmpty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue(PASSWORD, "Size.userForm.password");
        }

        if (!user.getEmail().contains(AT) || !user.getEmail().contains(".")) {
            errors.rejectValue(EMAIL, "invalid.email");
        }
    }
}
