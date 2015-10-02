package ciss.in.off.supplier;

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

import ciss.in.security.ReCaptchaResponseVerfier;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SupplierInfoController extends WebMvcConfigurerAdapter {
	@Autowired
	SupplierRepository supplierRepository;
	
	static final Logger logger = LoggerFactory.getLogger(SupplierInfoController.class);
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/supplier/register").setViewName("/supplier/register");
        registry.addViewController("/supplier/login").setViewName("/supplier/login");
        registry.addViewController("/supplier/data").setViewName("/supplier/data");        
    }

    @RequestMapping(value="/supplier/data", method=RequestMethod.GET)
    public String showMsgForm(Model model) {
    	//Map<String, Object> supplierMsg = new HashMap<String, Object>();
    	//SupplierInfo supplierMsg = new SupplierInfo();
    	//model.addAttribute("suppliermsg", supplierMsg);
    	return "/supplier/data";
    }
    
    @RequestMapping(value="/supplier/login", method=RequestMethod.GET)
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
    		returnValue = "/supplier/login";
    	}
    	else return "redirect:/";
		return returnValue;
    }
    
	/* Register */
    @RequestMapping(value="/supplier/register", method=RequestMethod.GET)
    public String showForm(Model model) {
    	Supplier supplier = new Supplier();
    	model.addAttribute(supplier);
 
    	String username;
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (principal instanceof UserDetails) {
    	  username = ((UserDetails)principal).getUsername();
    	} else {
    	  username = principal.toString();
    	}
    	
    	if (username.equals("anonymousUser") || username.equals(null)) {
    		return "/supplier/register";
    	}
    	else return "redirect:/";
    }

    @RequestMapping(value="/supplier/register", method=RequestMethod.POST)
    public String addSupplier(@ModelAttribute(value="supplier") @Valid Supplier supplier, BindingResult bindingResultSupplier, Model model, HttpServletRequest httpServletRequest) {

    	SupplierService supplierservice = new SupplierService(supplierRepository);
    	String returnValue = null;

    	if (bindingResultSupplier.hasErrors()) { 	
        	model.addAttribute("WelcomeMessage","Enter valid User ID aand password");      	
        	return "/supplier/register";
        }
    	
    	Supplier supplierNew = supplierRepository.findByUsername(supplier.getUsername());
    	Supplier suppliereMailNew = supplierRepository.findByEmail(supplier.getEmail());
        if ((supplierNew != null) || suppliereMailNew != null) {
        	model.addAttribute("WelcomeMessage","This userId or email is already taken, try a new one");
        	return "/supplier/register";
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
        		supplierservice = new SupplierService(supplierRepository);
	        	supplier.setPassword(new BCryptPasswordEncoder().encode(supplier.getPassword()));
	        	supplierservice.create(supplier);
	        	model.addAttribute(supplier);
	        	model.addAttribute("WelcomeMessage","Hi Welcome to our site");
	        	returnValue = "/supplier/data";
        	}
        	else returnValue = "/supplier/register";
        }
        return returnValue;
    }
}