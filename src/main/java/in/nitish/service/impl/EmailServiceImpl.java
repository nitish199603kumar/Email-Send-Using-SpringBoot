package in.nitish.service.impl;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import in.nitish.entity.EmailDetails;
import in.nitish.entity.EmailDtlsWithoutAttachment;
import in.nitish.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	@Override
	public String sendSimpleMail(EmailDtlsWithoutAttachment details) {

		try {

			// Creating a simple mail message
	//		SimpleMailMessage mailMessage = new SimpleMailMessage();
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);

			helper.setFrom(sender);
			helper.setTo(details.getRecipient());
			helper.setSubject(details.getSubject());
			helper.setText(details.getMsgBody());
			
			
			javaMailSender.send(mimeMessage);
			
			// Setting up necessary details
//			mailMessage.setFrom(sender);
//			System.out.println("Sender Mail : " +sender);
//			
//			mailMessage.setTo(details.getRecipient());
//			System.out.println("Send To : " +details.getRecipient());
//			
//			mailMessage.setText(details.getMsgBody());
//			System.out.println("Message Body : " +details.getMsgBody());
//			
//			mailMessage.setSubject(details.getSubject());
//			System.out.println("Subject :" +details.getSubject());
//
//			// Sending the mail
//			javaMailSender.send(mailMessage);
			return "Mail Sent Successfully...!!" ;
		}

		// Catch block to handle the exceptions
		catch (Exception e) {
//			System.out.print(e.printStackTrace());
			return "Error while Sending Mail...!!";
			
		}
	}

	@Override
	public String sendMailWithAttachment(EmailDetails details) {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {

			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(sender);
			mimeMessageHelper.setTo(details.getRecipient());
			mimeMessageHelper.setText(details.getMsgBody());
			mimeMessageHelper.setSubject(details.getSubject());

			FileSystemResource fileSystemResource = new FileSystemResource(new File(details.getAttachment()));

			mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
			javaMailSender.send(mimeMessage);

			return "Mail Sent Successfully";

		} catch (Exception e) {

			return "Error While Sending Mail";

		}

	}

}
