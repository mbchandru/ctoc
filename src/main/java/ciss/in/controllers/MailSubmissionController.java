package ciss.in.controllers;

import java.util.Date;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

import ciss.in.models.EmailInfo;
import ciss.in.utils.MailService;

@Controller
class MailSubmissionController/* extends WebMvcConfigurerAdapter*/ {

	@Autowired
	private JavaMailSender javaMailSender;

    @Autowired
    MailSubmissionController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RequestMapping(value="/user/contact", method=RequestMethod.GET)
    public String showLoginForm(Model model) {
    	EmailInfo emailForm = new EmailInfo();
    	model.addAttribute("emailForm",emailForm);
		return "/user/contact";
    } 

    @RequestMapping(value="/user/mail", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String sendEmail(HttpServletRequest request,
            HttpServletResponse response, Locale locale, @ModelAttribute(value="emailForm") @Valid EmailInfo emailForm, BindingResult bindingResultContact, Model model) throws MessagingException {        

    	if (bindingResultContact.hasErrors()) { 	
        	model.addAttribute("msg","Enter valid email id");      	
        	return "/user/contact";
        }
    	
    	try {
    		SimpleMailMessage mailMessage = new SimpleMailMessage();
	        mailMessage.setTo("admin@ciss.in");
	        mailMessage.setFrom("admin@ciss.in");
	        mailMessage.setReplyTo("admin@ciss.in");
	        mailMessage.setCc("admin@ciss.in");
	        emailForm.setSubject("Hi Customer - Thank you for your valuable feedback. We will work on your information and reply back as soon as possible.");
	        mailMessage.setSubject(emailForm.getSubject());
	        mailMessage.setSentDate(new Date());
	        mailMessage.setText(emailForm.getEmail() + " " + emailForm.getNumber() + " " + emailForm.getMessage());
	        //javaMailSender.send(mailMessage);
	        MailService mailserve = new MailService(javaMailSender);
	        ServletContext context = request.getServletContext();
	        mailserve.emailTemplate(emailForm, request, response, context, new Locale("en", "US"), "Admin", "admin@ciss.in");
	        model.addAttribute("msg", "Email successfully sent...");
	        return "/user/contact";
    	} catch (MailSendException send) {
    		model.addAttribute("msg", "Email id can be wrong, please check your email and submit...");
        }
		return "/user/contact";
    }
}