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

	public final static String TO_MAP = "/toMap";

	public final static String EDIT_USER = "/edit/user";

	public static final String DELETE_USER = "/delete/user";

	public static final String ADD_DOCUMENT = "/add/document";

	public static final String DOWNLOAD_DOCUMENT = "/download/document";

	public static final String DELETE_DOCUMENT = "/delete/document";

	public static final String ENGLISH_LANG = "?language=en";

	public static final String FRENCH_LANG = "?language=fr";

	public static final String CHECK_EQUALITY = "checkEquality";

	public static final String CHECK_LENGTH = "checkLength";

	public static final String CHECK_EMAIL = "checkEmail";

	// views
	public static final String LOGIN_VIEW = "login";

	public static final String REGISTRATION_VIEW = "registration";

	public static final String WELCOME_REDIRECT_VIEW = "redirect:/welcome";

	public static final String EDIT_USER_REDIRECT_VIEW = "redirect:/edit/user";

	public static final String ADD_DOC_REDIRECT_VIEW = "redirect:/add/document";

	public static final String WELCOME_VIEW = "welcomeUser";

	public static final String EDIT_USER_VIEW = "editUser";

	public static final String MANAGE_DOC_VIEW = "manageDocuments";

	public static final String REGISTRATION_SUCCESS_VIEW = "registrationSuccess";

	public static final String DELETE_SUCCESS_VIEW = "deleteSuccess";

}
