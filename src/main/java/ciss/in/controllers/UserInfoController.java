package ciss.in.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import ciss.in.models.User;
import ciss.in.repositories.UserRepository;
import ciss.in.security.ReCaptchaResponseVerfier;
import ciss.in.service.UserService;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

@Controller
public class UserInfoController extends WebMvcConfigurerAdapter {
	@Autowired
	UserRepository userRepository;
	
	static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user/register").setViewName("/user/register");
        registry.addViewController("/user/login").setViewName("/user/login");
        registry.addViewController("/user/data").setViewName("/user/data");
        registry.addViewController("/user/account").setViewName("/user/account");
    }
 
    @RequestMapping(value="/user/data", method=RequestMethod.GET)
    public String showMsgForm(Model model) {
    	User userMsg = new User();
    	model.addAttribute("usermsg", userMsg);
    	return "/user/data";
    }
    
    @RequestMapping(value="/user/login", method=RequestMethod.GET)
    public String doLoginForm(Model model) {
    	
    	String username;
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (principal instanceof UserDetails) {
    	  username = ((UserDetails)principal).getUsername();
    	} else {
    	  username = principal.toString();
    	}
    	
    	String returnValue = null;
    	if (username.equals("anonymousUser") || username.equals(null)) {
    		returnValue = "/user/login";
    	}
    	else return "redirect:/";
		return returnValue;
    }
    
	/* Register */
    @RequestMapping(value="/user/register", method=RequestMethod.GET)
    public String showForm(Model model) {
    	User user = new User();
    	model.addAttribute(user);
 
    	String username;
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (principal instanceof UserDetails) {
    	  username = ((UserDetails)principal).getUsername();
    	} else {
    	  username = principal.toString();
    	}
    	
    	if (username.equals("anonymousUser") || username.equals(null)) {
    		return "/user/register";
    	}
    	else return "redirect:/";
    }

    @RequestMapping(value="/user/register", method=RequestMethod.POST)
    public String addUser(@ModelAttribute(value="user") @Valid User user, BindingResult bindingResultUser, Model model, HttpServletRequest httpServletRequest) {

    	UserService userservice = new UserService(userRepository);
    	String returnValue = null;

    	if (bindingResultUser.hasErrors()) { 	
        	model.addAttribute("WelcomeMessage","Enter valid User ID aand password");      	
        	return "/user/register";
        }
    	
    	User userNew = userRepository.findByUsername(user.getUsername());
    	User usereMailNew = userRepository.findByEmail(user.getEmail());
    	
        if ((userNew != null) || usereMailNew != null) {
        	model.addAttribute("WelcomeMessage","This userId or email is already taken, try a new one");
        	return "/user/register";
        }
        else {
            boolean verify = false;
        	
          	String gRecaptchaResponse = (String) httpServletRequest.getParameter("g-recaptcha-response");
            ReCaptchaResponseVerfier veri = new ReCaptchaResponseVerfier();
			try {
				verify = veri.verifyRecaptcha(gRecaptchaResponse);
			} catch (IOException e) {
			}

        	if (verify) {
	        	userservice = new UserService(userRepository);
	        	user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
	        	user.setRole(2);
	        	userservice.create(user);
	        	model.addAttribute(user);
	        	model.addAttribute("WelcomeMessage","Hi Welcome to our site");
	        	returnValue = "redirect:/";
        	}
        	else returnValue = "/user/register";
        }
        return returnValue;
    }
    
    @RequestMapping(value="/user/account", method=RequestMethod.GET)
    public String showAccountForm(Model model) {
    	User useraccount = new User();
    	model.addAttribute("useraccount", useraccount);
    	return "/user/account";
    }    
}