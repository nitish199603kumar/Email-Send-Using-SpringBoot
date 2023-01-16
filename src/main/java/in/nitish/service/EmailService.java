package in.nitish.service;

import in.nitish.entity.EmailDetails;
import in.nitish.entity.EmailDtlsWithoutAttachment;

public interface EmailService {
	
	
	// To send a simple email
    String sendSimpleMail(EmailDtlsWithoutAttachment details);
    String sendMailWithAttachment(EmailDetails details);

}
