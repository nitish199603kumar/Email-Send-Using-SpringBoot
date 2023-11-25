package in.nitish.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;

import in.nitish.entity.EmailDetails;
import in.nitish.entity.EmailDtlsWithoutAttachment;
import in.nitish.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {
	
	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);


	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;
	
	@Autowired
	SpringTemplateEngine templateEngine;


	@Override
	public String sendSimpleMail(EmailDtlsWithoutAttachment details) {
		
		System.out.println("sendSimpleMail");

		try {

			// Creating a simple mail message
	//		SimpleMailMessage mailMessage = new SimpleMailMessage();
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
			helper.setFrom(sender);
			helper.setTo(details.getRecipient());
			helper.setSubject(details.getSubject());
			helper.setText(details.getMsgBody());
			
			
//			helper.setBcc("");
//			helper.setCc("");
			
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
			//send html content into text body and convert html page into pdf and send attachment
			String firstName="Nitish";
			String lastName="Kumar";
			String htmlData =sendDataToHtmlPage(firstName,lastName);
//			String htmlConvertToPdf=htmlToPdf(htmlData);
//			System.out.println(htmlConvertToPdf);
			
			byte[] convertHtmlToPdf=generatePdf(htmlData);
			
			
			mimeMessageHelper.setText(htmlData,true); //if you will not mention true here you will get html tag in template
//			mimeMessageHelper.setText(details.getMsgBody());
			mimeMessageHelper.setSubject(details.getSubject());

//			FileSystemResource fileSystemResource = new FileSystemResource(new File(details.getAttachment()));
//			mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
			System.out.println("attachment " +convertHtmlToPdf);
			mimeMessageHelper.addAttachment("test.pdf", new ByteArrayResource(convertHtmlToPdf));
			javaMailSender.send(mimeMessage);

			return "Mail Sent Successfully";

		} catch (Exception e) {
			
			return "Error While Sending Mail";

		}

	}
	private byte[] generatePdf(String htmlData) {
		
		FileOutputStream fileOutputStream=null;
	
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		try {
			fileOutputStream=new FileOutputStream(new File("C:\\Users\\NKSK\\Desktop\\234556789.pdf"));
			PdfWriter pdfWriter=new PdfWriter(byteArrayOutputStream);
			DefaultFontProvider defaultFontProvider=new DefaultFontProvider(true,false,false);
			ConverterProperties converterProperties=new ConverterProperties();
			converterProperties.setFontProvider(defaultFontProvider);
			HtmlConverter.convertToPdf(htmlData, pdfWriter,converterProperties);
			System.out.println("pdf generated");
			fileOutputStream.write(byteArrayOutputStream.toByteArray());
			System.out.println("Pdf write successfully" );
			return byteArrayOutputStream.toByteArray();
	}catch (Exception e) {
		e.printStackTrace();
		System.out.println("Exception in pdf generation");
	}
	return null;
}



	private String sendDataToHtmlPage(String firstName, String lastName) {
		Context context=new Context();
		context.setVariable("firstName", firstName);
		context.setVariable("lastName", lastName);
		return templateEngine.process("template.html", context);
	}

	public String htmlToPdf(String htmlContent) {
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		try {
			PdfWriter pdfWriter=new PdfWriter(byteArrayOutputStream);
			DefaultFontProvider defaultFontProvider=new DefaultFontProvider(true,false,false);
			ConverterProperties converterProperties=new ConverterProperties();
			converterProperties.setFontProvider(defaultFontProvider);
			HtmlConverter.convertToPdf(htmlContent, pdfWriter,converterProperties);
			
			LocalTime localTime=LocalTime.now();
			System.out.println("Current Time " +localTime);
			String randomId=UUID.randomUUID().toString();
			
			FileOutputStream fout=new FileOutputStream("F:\\OLD DESKTOP DATA\\SPRING BOOT PRACTICAL\\14-Email-Send-Using-SpringBoot\\src\\main\\resources\\templates\\PDF\\" + randomId +".pdf");
			byteArrayOutputStream.writeTo(fout);
			byteArrayOutputStream.close();
			byteArrayOutputStream.flush();
			fout.close();
			System.out.println("created");
			return "created";
		} catch (Exception e1) {
//			System.out.println(e1.printStackTrace(););
		}
		
		return "not created";
		
	}

}
