package com.clubmatrix.demo.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    @Value("${twilio.accountSid}")
    private String twilioSid;

    @Value("${twilio.authToken}")
    private String twilioAuthToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    private final Map<String, String> otpStorage = new HashMap<>();

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    private String normalizePhoneNumber(String phone) {
        phone = phone.replaceAll("[^\\d]", "");
        if (!phone.startsWith("+")) {
            phone = "+91" + phone; // Default India, change if needed
        }
        return phone;
    }

    public boolean sendOtp(String phone) {
        String otp = generateOtp();
        String normalizedPhone = normalizePhoneNumber(phone);
        otpStorage.put(normalizedPhone, otp);

        try {
            Twilio.init(twilioSid, twilioAuthToken);
            Message.creator(
                    new PhoneNumber(normalizedPhone),
                    new PhoneNumber(twilioPhoneNumber),
                    "Your OTP for login/sign up to Club Matrix is: " + otp
            ).create();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyOtp(String phone, String otp) {
        String normalizedPhone = normalizePhoneNumber(phone);
        String storedOtp = otpStorage.get(normalizedPhone);

        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(normalizedPhone);
            return true;
        }
        return false;
    }
}
