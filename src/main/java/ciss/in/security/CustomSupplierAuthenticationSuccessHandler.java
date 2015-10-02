package ciss.in.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@Configuration
public class CustomSupplierAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication)
            throws IOException, ServletException {
        //do some logic here if you want something to be done whenever
        //the user successfully logs in.
      	String gRecaptchaResponse = (String) httpServletRequest.getParameter("g-recaptcha-response");
        ReCaptchaResponseVerfier veri = new ReCaptchaResponseVerfier();
        boolean verify = veri.verifyRecaptcha(gRecaptchaResponse);
        
    	HttpSession session = httpServletRequest.getSession();
    	String returnValue = null;

    	if (verify) {
    		User supplierAuthUser;
    	
    	Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
    	if (obj instanceof User) {
    		supplierAuthUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            session.setAttribute("suppliername", supplierAuthUser.getUsername());
            session.setAttribute("authorities", authentication.getAuthorities());
            session.setAttribute("sessionId", sessionId);
            returnValue = "/supplier/data";
        }
    	System.out.println("sessionIdS " + sessionId);

        //set our response to OK status
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        
/*    	String userName = null;
    	if (!authUser.getUsername().equals("anonymousUser") || !authUser.getUsername().equals(null)) {
	    	Object principal1 = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	if (principal1 instanceof CustomUserDetails) {
	    		userName = ((CustomUserDetails)principal1).getUsername();
	    	}
    	}
        session.setAttribute("userName", userName);*/
    	returnValue = httpServletRequest.getContextPath() + returnValue;
		} else {
		    //System.out.println("<font color=red>You missed the Captcha.</font>");

		    session.invalidate();
		    SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
		    returnValue = "/supplier/login";
	    	returnValue = httpServletRequest.getContextPath() + returnValue;
		}
        //since we have created our custom success handler, its up to us to where
        //we will redirect the user after successfully login
        httpServletResponse.sendRedirect(returnValue);
    }
}