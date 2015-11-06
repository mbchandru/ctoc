package ciss.in.utils;

import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import ciss.in.models.EmailInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class MailService {

	static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private JavaMailSender javaMailSender;
	
	@Autowired
    private SpringTemplateEngine templateEngine;
	
	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender= javaMailSender;
		templateEngine = new SpringTemplateEngine();
        Set<ITemplateResolver> templatesResolvers = new HashSet<ITemplateResolver>();
        
        ServletContextTemplateResolver emailTemplateResolver = new ServletContextTemplateResolver();
        emailTemplateResolver.setPrefix("/thymeleaf/mails/");
        emailTemplateResolver.setTemplateMode("HTML5");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        emailTemplateResolver.setOrder(1);
        templatesResolvers.add(emailTemplateResolver);
        
        templatesResolvers.add(emailTemplateResolver);
        templateEngine.setTemplateResolvers(templatesResolvers);
	}
	public String emailSend(EmailInfo emailForm, String sender, String receiver, String subject, 
			String sendersEmail, String senderMobile, String messageType) {
		String result;
		
    	try {
    		SimpleMailMessage mailMessage = new SimpleMailMessage();
	        mailMessage.setTo(receiver);
	        mailMessage.setFrom(sender);
	        mailMessage.setReplyTo(sender);
	        mailMessage.setCc("admin@ciss.in");
	        emailForm.setSubject("Hi Customer - Thank you for your valuable feedback. We will work on your information and reply back as soon as possible.");
	        mailMessage.setSubject(emailForm.getSubject());
	        mailMessage.setSentDate(new Date());
	        //System.out.println("cc " + emailForm.getEmail() + " " + emailForm.getNumber() + " " + emailForm.getMessage());
	        mailMessage.setText(sendersEmail + " " + senderMobile + " " + messageType);
	        javaMailSender.send(mailMessage);
	        result = "Email successfully sent...";
    	} catch (MailSendException send) {
    		result = "eeEmail id can be wrong, please check your email and submit...";
        }
		return result;
	}
	
	public void emailTemplate(EmailInfo emailForm, HttpServletRequest request,
            HttpServletResponse response, ServletContext context, Locale locale,
            String recipientName, final String recipientEmail/*, final String imageResourceName, 
		final byte[] imageBytes, final String imageContentType, final Locale locale*/)
		throws MessagingException {
		 
		// Prepare the evaluation context
		final WebContext ctx = new WebContext(request, response, context, locale);
		//final Context ctx = new Context(locale);
		ctx.setVariable("name", recipientName);
		ctx.setVariable("subscriptionDate", new Date());
		ctx.setVariable("transactions", emailForm.getTransactionTypes());
		ctx.setVariable("products", emailForm.getProducts());
		//ctx.setVariable("imageResourceName", imageResourceName); // so that we can reference it from HTML
		 
		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
		final MimeMessageHelper message =
		new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
		message.setSubject("CXC email : Your transaction's request information");
		message.setFrom("admin@ciss.in");
		message.setTo(recipientEmail);

		final String htmlContent = this.templateEngine.process("mailsimple", ctx);
		message.setText(htmlContent, true); // true = isHtml
		 
		// Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
		//final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
		//message.addInline(imageResourceName, imageSource, imageContentType);
        System.out.println("cc " + emailForm.getProducts().get(1) + " " + emailForm.getNumber() + " " + emailForm.getMessage());
		 
		// Send mail
		this.javaMailSender.send(mimeMessage);
	}
}