package com.egs.account.mapping;

/**
 * Created by User on 12.03.2017.
 */
public class UrlMapping {

	public static final String ROOT = "/";

	public final static String LOGIN = "/login";

	public final static String LOGOUT = "/logout";

	public final static String WELCOME = "/welcome";

	public final static String REGISTRATION = "/registration";

    public final static String REGISTRATION_CONFIRM = "/registrationConfirm";

	public final static String TO_MAP = "/toMap";

	public final static String EDIT_USER = "/edit/user";

	public static final String DELETE_USER = "/delete/user";

	public static final String ADD_DOCUMENT = "/add/document";

	public static final String DOWNLOAD_DOCUMENT = "/download/document";

	public static final String DELETE_DOCUMENT = "/delete/document";

    public static final String CHECK_USERNAME = "checkUsername";

	public static final String CHECK_EMAIL = "checkEmail";

    public static final String CHECK_EMAIL_EDIT = "edit/user/checkEmail";

    public static final String CHECK_PASSWORD = "checkPassword";

    public static final String CHECK_PASSWORD_EDIT = "edit/user/checkPassword";

    public static final String CHECK_PASSWORD_CONFIRM = "checkPasswordConfirm";

	// views
	public static final String LOGIN_VIEW = "login";

	public static final String REGISTRATION_VIEW = "registration";

	public static final String WELCOME_REDIRECT_VIEW = "redirect:/welcome";

	public static final String EDIT_USER_REDIRECT_VIEW = "redirect:/edit/user";

	public static final String ADD_DOC_REDIRECT_VIEW = "redirect:/add/document";

    public static final String LOGIN_REDIRECT = "redirect:/login?language=";

    public static final String BAD_USER_REDIRECT = "redirect:/badUser?language=";

	public static final String WELCOME_VIEW = "welcomeUser";

	public static final String EDIT_USER_VIEW = "editUser";

	public static final String MANAGE_DOC_VIEW = "manageDocuments";

	public static final String REGISTRATION_SUCCESS_VIEW = "registrationSuccess";

	public static final String DELETE_SUCCESS_VIEW = "deleteSuccess";

    public static final String EMAIL_ERROR_VIEW = "error/emailError";

}
