package com.javaportal.service;

import com.javaportal.exception.JavaPortalException;

public interface ForgotPasswordService {
	public void sendOtpMail(String toEmail, String otp) throws JavaPortalException;
}
