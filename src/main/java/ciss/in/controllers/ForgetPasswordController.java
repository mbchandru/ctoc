package ciss.in.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ciss.in.models.User;
import ciss.in.repositories.UserRepository;


@Controller
public class ForgetPasswordController extends WebMvcConfigurerAdapter {

	@Autowired
	UserRepository userRepository;
	
	static final Logger logger = LoggerFactory.getLogger(ForgetPasswordController.class);
	
    private final JavaMailSender javaMailSender;

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user/forgotpassword").setViewName("/user/forgotpassword");
    }

    @Autowired
    ForgetPasswordController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

	@RequestMapping(value = "/user/forgotpassword", method = RequestMethod.GET)
	public String resetPasswordView(final Model model) {
		model.addAttribute("user", new User());
	    return "/user/forgotpassword";
	}

	@RequestMapping(value = "/user/forgotpassword", method = RequestMethod.POST)
	public String forgetPassword(@ModelAttribute User user, final Model model) throws MessagingException, IOException {
		model.addAttribute("user", user);

		logger.info("UserTransmittedEmail# :" + user.getEmail());
		User foundUser = userRepository.findByEmail(user.getEmail());

		if (foundUser != null) {

			String secureToken = UUID.randomUUID().toString();
			foundUser.setResetPasswordToken(secureToken);

			/*
				Give token one hour expiration delay
			 */
			Date currentDate = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			calendar.add(Calendar.MINUTE, 300); 

			Date expirationDate = calendar.getTime();
			logger.info("Expiration date :" + expirationDate);

			foundUser.setResetPasswordExpires(expirationDate);
			/*
				Update user into database
			 */
			//System.out.println("foundUser.getEmail() " + foundUser.getEmail());
			userRepository.save(foundUser);
			String text = "You are receiving this because you (or someone else) have requested the reset of the password for your account.\n\n"
							+ "Please click on the following link, or paste into your browser to complete the reset password process :\n\n"
							+ ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/resetpassword").queryParam("_key", secureToken).build().toUriString()
							+ "\n\n If you did not request this, please ignore this email and your password will remain unchanged.";
			sendResetPasswordLink(foundUser.getEmail(), text);
			String responseMessage = "An email has been sent to your registered email address, Please check";
			model.addAttribute("responseMessage", responseMessage);
			return "/user/forgotpassword";
		}
		else {
			String responseMessage = "Invalid email address.This account doesn't exist";
			model.addAttribute("invalidMailAddress", responseMessage);
			return "/user/forgotpassword";
		}
	}

	private void sendResetPasswordLink(String email, String text) throws MessagingException, IOException {
    	try {
    		SimpleMailMessage mailMessage = new SimpleMailMessage();
	        mailMessage.setTo(email);
	        mailMessage.setFrom("admin@ciss.in");
	        mailMessage.setReplyTo("admin@ciss.in");
	        mailMessage.setCc("admin@ciss.in");
	        
	        mailMessage.setSubject("Password reset request");
	        mailMessage.setSentDate(new Date());
	        mailMessage.setText(text);
	        javaMailSender.send(mailMessage);
    	} catch (MailSendException send) {
        }
    }
}