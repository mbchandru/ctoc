package ciss.in.controllers;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ciss.in.models.User;
import ciss.in.repositories.UserRepository;

@Controller
public class ResetPasswordController extends WebMvcConfigurerAdapter {

	static final Logger logger = LoggerFactory.getLogger(ResetPasswordController.class);

	@Autowired
	UserRepository userRepository;

   public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user/resetpassword").setViewName("/user/resetpassword");
        registry.addViewController("/user/error").setViewName("/user/error");
    }
	   
	@RequestMapping(value = "/user/resetpassword", method = RequestMethod.GET)
	public String resetpasswordView( @RequestParam(value = "_key") String resetPasswordToken, final Model model) {

		User user = userRepository.findByResetPasswordToken(resetPasswordToken);
    Date expirationDate;
    if (user != null) {
      expirationDate = user.getResetPasswordExpires();
      if (expirationDate.after(new Date())) {
    	user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("resetPasswordToken", resetPasswordToken);
        return "/user/resetpassword";
      }
    }
	else {
		return "/user/error";
	}
	return "/user/resetpassword";
  }

	@RequestMapping(value = "/user/resetpassword", method = RequestMethod.POST)
	public String resetPassword( @RequestParam(value = "_key") String resetPasswordToken, @ModelAttribute User user,
			final Model model) {
		logger.info("UserLogin# " + user.getEmail() + "  UserPassword# " + user.getPassword());

		User userToUpdate = userRepository.findByResetPasswordToken(resetPasswordToken);
		String updatedPassword = user.getPassword();
		userToUpdate.setPassword(encryptPassword(updatedPassword));
		
		userToUpdate.setResetPasswordToken(null);
		userToUpdate.setResetPasswordExpires(null);
		
		userRepository.save(userToUpdate);
    
		boolean passwordChanged = true;

		String redirectionUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/login").build().toUriString();

		String responseMessage = "Your password was successfully updated, Please login now";
		model.addAttribute("passwordChanged", passwordChanged);
		model.addAttribute("redirectionUrl", redirectionUrl);
		model.addAttribute("responseMessage", responseMessage);

		return "/user/login";
	}

	private String encryptPassword(String password){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}
}