package ciss.in.security;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.XmppClient;
import ciss.in.xmpp.XMPPConnection;

@Component
public class SessionTimeOutListener implements ApplicationListener<ApplicationEvent> {
	
	private XmppClient xmppClient;
	
	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if(applicationEvent instanceof SessionDestroyedEvent){		
	        for (SecurityContext ctx : ((SessionDestroyedEvent) applicationEvent).getSecurityContexts() ) {
	            Authentication authentication = ctx.getAuthentication();

	    		CustomUserDetails authUser = (CustomUserDetails) authentication.getPrincipal();
	    		if (authUser.getUsername() != null) {
			   		try {
			   	  		XMPPConnection xmpp = new XMPPConnection();
			   	  		xmpp.makeXmppClient();
			   	  		xmppClient = xmpp.getXmppClient();			   	  		
			   	  		xmpp.unregisterUser(xmppClient, authUser, authentication);
			   		} catch (XmppException e) {
			   			e.printStackTrace();
			   		}
	    		}
	       }
		}
	}
}