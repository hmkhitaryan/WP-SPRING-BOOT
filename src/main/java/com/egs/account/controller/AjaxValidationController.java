package com.egs.account.controller;

import com.egs.account.ajax.JsonResponse;
import com.egs.account.mapping.UrlMapping;
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

	private static final String FAIL = "FAIL";

	private static final String SUCCESS = "SUCCESS";

	@Autowired
	private ValidationService userValidator;

	@RequestMapping(value = UrlMapping.CHECK_EQUALITY, method = RequestMethod.POST)
	public @ResponseBody
    JsonResponse resolveEquality(@RequestParam(PASSWORD) String password,
                                 @RequestParam(PASSWORD_CONFIRM) String passwordConfirm) {
		return processValidate(!passwordConfirm.equals(password));
	}

	@RequestMapping(value = UrlMapping.CHECK_LENGTH, method = RequestMethod.POST)
	public @ResponseBody
    JsonResponse resolveLength(@RequestParam(PASSWORD) String password) {
		return processValidate(userValidator.isFieldLengthInvalid(password, 8, 32));
	}

	@RequestMapping(value = UrlMapping.CHECK_EMAIL, method = RequestMethod.POST)
	public @ResponseBody
    JsonResponse resolveEmailValidation(@RequestParam(EMAIL) String email) {
		return processValidate(userValidator.isInvalidEmail(email));
	}

	private JsonResponse processValidate(boolean negativeExp) {
		JsonResponse res = new JsonResponse();
		if (negativeExp) {
			res.setStatus(FAIL);
		} else {
			res.setStatus(SUCCESS);
		}
		return res;
	}
}
