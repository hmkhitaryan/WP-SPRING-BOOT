package com.egs.account.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessionExpiredStrategy implements InvalidSessionStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionExpiredStrategy.class);

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (session.isNew()) {
            LOGGER.warn("Session is expired, redirecting login page...");
            response.sendRedirect("/login");
        }
    }
}
