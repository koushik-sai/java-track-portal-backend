package com.javaportal.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaportal.exception.JavaPortalException;
import com.javaportal.service.LoginService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping(value="/api/auth")
public class LoginAPI {
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private Environment env;
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null) {
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    // Clear JSESSIONID cookie by setting max age to 0
	    Cookie cookie = new Cookie("JSESSIONID", null);
	    cookie.setPath("/");
	    cookie.setHttpOnly(true);
	    cookie.setMaxAge(0);
	    response.addCookie(cookie);

	    // Clear remember-me cookie similarly
	    Cookie rememberMeCookie = new Cookie("remember-me", null);
	    rememberMeCookie.setPath("/");
	    rememberMeCookie.setHttpOnly(true);
	    rememberMeCookie.setMaxAge(0);
	    response.addCookie(rememberMeCookie);

	    return ResponseEntity.ok().build();
	}
	
	@PostMapping(value="/updatePassword/{emailId}/{password}")
	public ResponseEntity<String> updatePassword(@PathVariable String emailId, @PathVariable String password) throws JavaPortalException {
		String message = "";
		try {
			loginService.updatePassword(emailId, password);
			message = env.getProperty("Api.PASSWORD_UPDATED");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

