package ciss.in.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.context.request.RequestContextHolder;

import rocks.xmpp.core.session.XmppClient;
import ciss.in.Application;
import ciss.in.xmpp.XMPPConnection;
import ciss.in.xmpp.template.XmppUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@Configuration
public class CustomUserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private XmppClient xmppClient;
	
	private XmppUser xmppUser;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication)
            throws IOException, ServletException {
    	
        //do some logic here if you want something to be done whenever
        //the user successfully logs in.
/*      	String gRecaptchaResponse = (String) httpServletRequest.getParameter("g-recaptcha-response");
        ReCaptchaResponseVerfier veri = new ReCaptchaResponseVerfier();
        boolean verify = veri.verifyRecaptcha(gRecaptchaResponse);*/
        
    	boolean verify = true;
    	HttpSession session = httpServletRequest.getSession();
    	String returnValue = null;

    	if (verify) {
    		CustomUserDetails authUser = null;
    	
    	Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
    	if (obj instanceof User) {
    		authUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		
            session.setAttribute("username", authUser.getUsername());
            session.setAttribute("authorities", authentication.getAuthorities());
            session.setAttribute("sessionId", sessionId);
            returnValue = "/user/transact";
    	}
    	System.out.println("sessionId " + sessionId);
        //set our response to OK status
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        
  		// Create xmpp connection with ejabberd for logged in user
  		authentication = SecurityContextHolder.getContext().getAuthentication();

  		XMPPConnection xmpp = new XMPPConnection();
  		xmpp.makeXmppClient();
  		xmppClient = xmpp.getXmppClient();
  		
  		boolean registered = xmpp.registerUser(xmppClient, authUser);
  		if (registered) {
  			xmppUser = new XmppUser();
  			String username = authUser.getUsername();
  			String password = authUser.getPassword();
  			
  			xmppUser = xmpp.loginUser(xmppClient, username, password, Application.xmppConfig.getHost());
  			session.setAttribute("xmppUser", xmppUser);
  		}
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
		    returnValue = "/user/login";
	    	returnValue = httpServletRequest.getContextPath() + returnValue;
		}
        //since we have created our custom success handler, its up to us to where
        //we will redirect the user after successfully login
        httpServletResponse.sendRedirect(returnValue);
    }
}