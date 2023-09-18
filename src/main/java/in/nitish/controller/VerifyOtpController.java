package in.nitish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.nitish.entity.EmailDtlsWithoutAttachment;
import in.nitish.service.EmailService;

@RestController
@RequestMapping("/email")
public class VerifyOtpController {

	@Autowired
	EmailService emailService;

	// Sending a simple Email to verify smartcontactmanager otp verification
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestBody EmailDtlsWithoutAttachment details) {
		System.out.println("verifyOTP Request " +details);
		String status = emailService.sendSimpleMail(details);
		System.out.println("Status --> " +status);
		return status;
	}

	
}
