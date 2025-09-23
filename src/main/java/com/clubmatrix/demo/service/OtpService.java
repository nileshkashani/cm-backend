package com.clubmatrix.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    // Note: For production, use a persistent store like Redis or a database table.
    private final Map<String, String> otpStorage = new HashMap<>();

    /**
     * Generates a 6-digit OTP.
     */
    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    /**
     * Sends an OTP to the specified email address.
     * This method now matches the one called by your controller.
     */
    public boolean sendOtp(String email) {
        String otp = generateOtp();
        String normalizedEmail = email.trim().toLowerCase();
        otpStorage.put(normalizedEmail, otp);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("youremail@gmail.com"); // The email you configured
            message.setTo(email);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp);
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifies the provided OTP for the given email.
     */
    public boolean verifyOtp(String email, String otp) {
        String normalizedEmail = email.trim().toLowerCase();
        String storedOtp = otpStorage.get(normalizedEmail);
        
        // Important: After successful verification, remove the OTP to prevent reuse.
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(normalizedEmail);
            return true;
        }
        return false;
    }
}
