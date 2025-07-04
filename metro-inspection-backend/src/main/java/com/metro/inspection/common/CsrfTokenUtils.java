package com.metro.inspection.common;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class CsrfTokenUtils {

    /**
     * 从请求中获取CSRF Token
     */
    public String getCsrfToken(HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        return token != null ? token.getToken() : null;
    }

    /**
     * 验证CSRF Token
     */
    public boolean validateCsrfToken(HttpServletRequest request, String token) {
        String expectedToken = getCsrfToken(request);
        return expectedToken != null && expectedToken.equals(token);
    }
} 