package com.javaportal.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.javaportal.entity.Employee;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.repository.EmployeeRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private EmployeeRepository empRepo;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordServiceImpl.class);

    @Override
    public void sendOtpMail(String toEmail, String otp) throws JavaPortalException {
    	
    	Optional<Employee> optEmp = empRepo.findByEmailId(toEmail);
    	Employee emp = optEmp.orElseThrow(() -> new JavaPortalException("Service.EMPOLYEE_NOT_FOUND"));
    	
        String subject = "Password Reset Request - Your OTP Code";

        // HTML email content with styled OTP digits
        String htmlMsg = "<div style=\"font-family: Arial, sans-serif; font-size: 16px; color: #333;\">"
                + "<p>Dear " + emp.getName() + "</p>"
                + "<p>We received a request to reset your password. Use the following One-Time Password (OTP) to proceed:</p>"
                + "<p style=\"font-size: 28px; font-weight: bold; letter-spacing: 6px; color: #2e6c80; margin: 20px 0;\">"
                + otp
                + "</p>"
                + "<p>This OTP is valid for the next 5 minutes. Please do not share it with anyone.</p>"
                + "<p>If you did not request a password reset, please ignore this email or contact our support team immediately.</p>"
                + "<br>"
                + "<p>Best regards,<br>Java Track Portal</p>"
                + "</div>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlMsg, true); // true = HTML email

            mailSender.send(message);

            logger.info("OTP email sent successfully to {}", toEmail);

        } catch (Exception e) {
            logger.error("Failed to send OTP email to {}: {}", toEmail, e.getMessage(), e);
            throw new JavaPortalException("Email sending failed: " + e.getMessage());
        }
    }
}

