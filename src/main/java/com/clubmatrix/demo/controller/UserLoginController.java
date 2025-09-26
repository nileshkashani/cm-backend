package com.clubmatrix.demo.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clubmatrix.demo.service.OtpService;
import com.clubmatrix.demo.model.UserLogin;
import com.clubmatrix.demo.repository.UserLoginRepository;

@RestController
@RequestMapping("/login")

public class UserLoginController {
	@Autowired
	private OtpService otpService;

	@Autowired
	private UserLoginRepository userLoginRepository;

	@PostMapping("/send/otp")
	public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
		String phoneNo = request.get("phoneNo");
		if (phoneNo == null || phoneNo.isBlank()) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "phoneNo is required!"));
		}
		boolean sent = otpService.sendOtp(phoneNo);
		return sent ? ResponseEntity.ok(Map.of("success", true, "message", "otp sent successfully!"))
				: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(Map.of("success", false, "message", "Error sending otp!"));
	}

	@PostMapping("/verify/otp")
	public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
		String phoneNo = request.get("phoneNo");
		String otp = request.get("otp");

		if (phoneNo == null || phoneNo.isBlank() || otp == null || otp.isBlank()) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "OTP is required!"));
		}

		UserLogin user = userLoginRepository.findByPhoneNo(phoneNo);
		if (user == null) {
            return ResponseEntity.status(404) // Use 404 Not Found for non-existent users
                            .body(Map.of("success", false, "message", "User not registered, please register first!"));
		}

		boolean verified = otpService.verifyOtp(phoneNo, otp);

		return verified ? ResponseEntity.ok(Map.of("success", true, "message", "otp verified successfully", "user", user))
				: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(Map.of("success", false, "message", "Invalid otp!"));
	}
	@PostMapping("/verify/otp/for/register")
	public ResponseEntity<?> verifyOtpForRegister(@RequestBody Map<String, String> request) {
		String phoneNo = request.get("phoneNo");
		String otp = request.get("otp");

		if (phoneNo == null || phoneNo.isBlank() || otp == null || otp.isBlank()) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "OTP is required!"));
		}

		boolean verified = otpService.verifyOtp(phoneNo, otp);

		return verified ? ResponseEntity.ok(Map.of("success", true, "message", "otp verified successfully" ))
				: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(Map.of("success", false, "message", "Invalid otp!"));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody UserLogin user) {
		try {
			UserLogin u2 = userLoginRepository.findByEmail(user.getEmail());
			
			if (u2 != null && u2.getEmail().equals(user.getEmail())) {
				return ResponseEntity.badRequest()
						.body(Map.of("success", false, "message", "user already exists, please login!"));
			}

			UserLogin savedUser = userLoginRepository.save(user);

			return ResponseEntity.ok(Map.of("success", true, "message", "user registered successfully!", "user", savedUser));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "Error: " + e.getMessage()));
		}

	}

	@PostMapping("/password")
public ResponseEntity<?> userLogin(@RequestBody Map<String, String> request) {
    try {
        String email = request.get("email");
        String password = request.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.status(400)
                            .body(Map.of("success", false, "message", "Email and password required!"));
        }

        UserLogin user = userLoginRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404) // Use 404 Not Found for non-existent users
                            .body(Map.of("success", false, "message", "User not registered, please register first!"));
        }

        // --- THIS IS THE CRITICAL PASSWORD CHECK ---
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401) // Use 401 Unauthorized for bad credentials
                            .body(Map.of("success", false, "message", "Invalid email or password!"));
        }
        // -----------------------------------------

        return ResponseEntity
                        .ok(Map.of("success", true, "message", "User logged in successfully!", "user", user));
    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of("success", false, "message", "Error: " + e.getMessage()));
    }
}
}