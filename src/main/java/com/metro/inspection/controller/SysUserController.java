package com.metro.inspection.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @GetMapping("/test-response")
    public void testResponse(HttpServletResponse response) throws java.io.IOException {
        response.setContentType("text/plain");
        response.getWriter().write("ok");
        response.flushBuffer();
    }
} 