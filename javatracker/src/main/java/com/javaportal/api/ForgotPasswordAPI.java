package com.javaportal.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaportal.service.ForgotPasswordService;
import com.javaportal.service.OtpService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class ForgotPasswordAPI {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/request-otp/{emailId}")
    public ResponseEntity<String> requestOtp(@PathVariable String emailId) {
        try {
            String otp = otpService.generateOtp(emailId); // You can also log for debug
            forgotPasswordService.sendOtpMail(emailId, otp);
            String message = "Api.OTP_SENT_SUCCESS";
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to send OTP: " + e.getMessage());
        }
    }


    @PostMapping("/verify-otp/{emailId}/{otp}")
    public ResponseEntity<String> verifyOtp(@PathVariable String emailId,
                                            @PathVariable String otp) {
    	try {
        if (otpService.validateOtp(emailId, otp)) {
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Invalid or expired OTP.");
        }
        } catch(Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP: " + e.getMessage());
        }
    }
}

