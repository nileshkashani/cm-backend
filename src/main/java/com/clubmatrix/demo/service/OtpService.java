package com.clubmatrix.demo.service;

import java.util.HashMap;
import java.util.Random;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
	
	private final JavaMailSender javaMailSender;
	public OtpService(JavaMailSender mailSender) {
		this.javaMailSender = mailSender;
	}
	private final HashMap<String, String> mp = new HashMap<>();
	
	public boolean sendOtp(String email){
		String otp = String.format("%04d", new Random().nextInt(10000));
		String newEmail = email.toLowerCase();
		mp.put(newEmail, otp);
		
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(newEmail);
			mailMessage.setSubject("OTP for Club Matrix");
			mailMessage.setText("your otp is: "+otp);
			javaMailSender.send(mailMessage);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	public boolean verifyOtp(String email, String otp) {
		String newEmail = email.toLowerCase();
		String otpStored = mp.get(newEmail);
		
		if(otpStored == null) {
			return false;
		}
		return otpStored.equals(otp); 
	}
}
