package com.javaportal.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final Map<String, OtpDetails> otpCache = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(1000000));

        OtpDetails otpDetails = new OtpDetails(otp, LocalDateTime.now(), false);
        otpCache.put(email, otpDetails);

        // Optionally, schedule cleanup after 1 minute
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            OtpDetails details = otpCache.get(email);
            if (details != null && !details.isUsed() && details.isExpired()) {
                otpCache.remove(email);
            }
        }, 5, TimeUnit.MINUTES);

        return otp;
    }

    public boolean validateOtp(String email, String inputOtp) {
        OtpDetails otpDetails = otpCache.get(email);
        if (otpDetails == null) return false;

        if (otpDetails.isUsed() || otpDetails.isExpired()) {
            otpCache.remove(email); // Clean up
            return false;
        }

        if (otpDetails.getOtp().equals(inputOtp)) {
            otpDetails.setUsed(true); // Mark as used
            return true;
        }

        return false;
    }

    // Nested class to store OTP info
    private static class OtpDetails {
        private final String otp;
        private final LocalDateTime generatedAt;
        private boolean used;

        public OtpDetails(String otp, LocalDateTime generatedAt, boolean used) {
            this.otp = otp;
            this.generatedAt = generatedAt;
            this.used = used;
        }

        public String getOtp() {
            return otp;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public boolean isExpired() {
            return generatedAt.plusMinutes(1).isBefore(LocalDateTime.now());
        }
    }
}


