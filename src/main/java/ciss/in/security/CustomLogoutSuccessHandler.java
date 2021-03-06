package ciss.in.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.XmppClient;
import ciss.in.xmpp.XMPPConnection;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	
	private XmppClient xmppClient;
	
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    	CustomUserDetails authUser = (CustomUserDetails) authentication.getPrincipal();

		try {
	  		XMPPConnection xmpp = new XMPPConnection();
	  		xmpp.makeXmppClient();
	  		xmppClient = xmpp.getXmppClient();
	  		
	  		xmpp.unregisterUser(xmppClient, authUser, authentication);
		} catch (XmppException e) {
			e.printStackTrace();
		}

		response.sendRedirect("user/login");
    }

}