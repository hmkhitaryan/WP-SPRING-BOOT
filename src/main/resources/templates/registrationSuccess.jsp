<%@ page import="com.egs.account.mapping.UrlMapping" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="LOGOUT_URL" value="<%=UrlMapping.LOGIN%>"/>
<c:set var="WELCOME_URL" value="<%=UrlMapping.WELCOME%>"/>
<c:set var="ADD_DOC_URL" value="<%=UrlMapping.ADD_DOCUMENT%>"/>
<c:set var="ENGLISH_LANG_URL" value="<%=UrlMapping.ENGLISH_LANG%>"/>
<c:set var="FRENCH_LANG_URL" value="<%=UrlMapping.FRENCH_LANG%>"/>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Registration Confirmation Page</title>
	<link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet"/>
	<link href="<c:url value='/resources/css/app.css' />" rel="stylesheet"/>
</head>
<body>
<div class="pull-right" style="padding-right:50px">
	<a href="${ENGLISH_LANG_URL}">English</a>|<a href="${ENGLISH_LANG_URL}">French</a>
</div>
<div class="generic-container">
	<div class="alert alert-success lead">
		${success}
	</div>
	<span class="well pull-left">
			<a href="<c:url value='${ADD_DOC_URL}/${userForm.id}' />"><spring:message code="button.upload.text"/></a>
		</span>
	<span class="well pull-right">Go to <a href="<c:url value='${WELCOME_URL}'/>"
										   class="btn btn-success custom-width"><spring:message
			code="button.yourPage.label"/></a>
			<a href="<c:url value='${LOGOUT_URL}'/>"
			   class="btn btn-success custom-width"><spring:message code="button.loginPage.label"/></a>
		</span>
</div>
</body>

</html>