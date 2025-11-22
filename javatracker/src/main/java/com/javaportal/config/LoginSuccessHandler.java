package com.javaportal.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaportal.dto.EmployeeDTO;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.service.LoginService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final LoginService employeeService;

    public LoginSuccessHandler(LoginService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String emailId = ((UserDetails) authentication.getPrincipal()).getUsername();

        EmployeeDTO employeeDto = null;
        try {
            employeeDto = employeeService.getEmployeeByEmailId(emailId);
        } catch (JavaPortalException e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(employeeDto);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}

