package com.egs.account.service.validator;

import com.egs.account.model.User;
import com.egs.account.service.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class ValidationService implements Validator {

    private static final String NOT_EMPTY = "NotEmpty";

    private static final String USERNAME = "username";

    private static final String PASSWORD_CONFIRM = "passwordConfirm";

    private static final String EMAIL = "email";

    private static final String AT = "@";

    private static final String PASSWORD = "password";

    private static final String REGEX_PASSWORD = "(?=.*?\\d)(?=.*?[a-zA-Z])(?=.*?[^\\w]).{8,}";

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        final User user = (User) o;
        if (!user.isUpdated()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, USERNAME, NOT_EMPTY);
            final String username = user.getUsername();

            if (isFieldLengthInvalid(username, 4, 25)) {
                errors.rejectValue(USERNAME, "Size.userForm.username");
            }

            final User alreadyExist = userService.findByUsername(username);
            if (alreadyExist != null) {
                errors.rejectValue(USERNAME, "Duplicate.userForm.username");
            }
            if (!user.getPasswordConfirm().equals(user.getPassword())) {
                errors.rejectValue(PASSWORD_CONFIRM, "Diff.passwordConfirm");
            }
        }

        validatePassword(errors, user.getPassword());

        if (isInvalidEmail(user.getEmail())) {
            errors.rejectValue(EMAIL, "invalid.email");
        }
    }

    public boolean isInvalidEmail(String email) {
        if (checkEmpty(email)) {
            return true;
        }
        return !email.contains(AT) || !email.contains(".");
    }

    public boolean isInvalidUsername(String username) {
        if (checkEmpty(username)) {
            return true;
        }
        return isFieldLengthInvalid(username, 4, 25);
    }

    public boolean isInvalidPassword(String password) {
        if (checkEmpty(password)) {
            return true;
        }
        if (isFieldLengthInvalid(password, 8, 25)) {
            return true;
        }

        return !password.matches(REGEX_PASSWORD);
    }

    public boolean passwordsMatch(String password, String passwordConfirm) {
        return StringUtils.equals(password, passwordConfirm);
    }

    private boolean checkEmpty(String field) {
        return StringUtils.isEmpty(field);
    }

    private void validatePassword(Errors errors, String password) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, PASSWORD, "NotEmpty");
        if (isInvalidPassword(password)) {
            errors.rejectValue(PASSWORD, "Size.password");
        }
    }

    private boolean isFieldLengthInvalid(String field, int lowerBound, int upperBound) {
        return field.length() < lowerBound || field.length() > upperBound;
    }
}
