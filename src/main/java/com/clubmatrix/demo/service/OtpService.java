package com.clubmatrix.demo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new HashMap<>();

    @Value("${twilio.accountSid}")
    private String twilioSid;

    @Value("${twilio.authToken}")
    private String twilioAuthToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    private String normalizeKey(String key) {
        return key.trim().toLowerCase();
    }

    private String normalizePhoneNumber(String phone) {
        phone = phone.replaceAll("[^\\d]", "");
        if (!phone.startsWith("+")) {
            phone = "+91" + phone; // default India, change if needed
        }
        return phone;
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    public boolean sendSmsOtp(String phone) {
        String otp = generateOtp();
        String normalizedPhone = normalizePhoneNumber(phone);
        otpStorage.put(normalizeKey(normalizedPhone), otp);

        try {
            Twilio.init(twilioSid, twilioAuthToken);
            Message.creator(
                    new PhoneNumber(normalizedPhone),
                    new PhoneNumber(twilioPhoneNumber),
                    "Your OTP is: " + otp
            ).create();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyOtp(String phone, String otp) {
        String normalizedPhone = normalizeKey(normalizePhoneNumber(phone));
        String storedOtp = otpStorage.get(normalizedPhone);
        return storedOtp != null && storedOtp.equals(otp);
    }
}
