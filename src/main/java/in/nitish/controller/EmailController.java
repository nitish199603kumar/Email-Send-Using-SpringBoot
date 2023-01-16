package in.nitish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.nitish.entity.EmailDetails;
import in.nitish.entity.EmailDtlsWithoutAttachment;
import in.nitish.service.EmailService;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;

	// Sending a simple Email
	@PostMapping("/sendMail")
	public String sendMail(@RequestBody EmailDtlsWithoutAttachment details) {

		String status = emailService.sendSimpleMail(details);
		System.out.println("Status --> " +status);
		return status;
	}

	@PostMapping("/sendMailWithAttachment")
	public String sendMailWithAttachment(@RequestBody EmailDetails details) {
		String status = emailService.sendMailWithAttachment(details);

		return status;
	}

}
