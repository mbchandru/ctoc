package ciss.in.xmpp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.extensions.httpbind.BoshConnectionConfiguration;
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;
import ciss.in.xmpp.template.XmppAuthenticationException;
import ciss.in.xmpp.template.XmppUser;
import ciss.in.xmpp.template.config.XmppConfig;

public class XMPPConnection {

	@Autowired
    private XmppConfig xmppConfig;
	
	public static XmppClient xmppClient;
    
	public XmppClient xmppConnect() {
		//XmppConfig xmppConfig = new XmppConfig();
		//System.out.println("xmppConfig.getHost() " + xmppConfig.getHost());
        //Authentication and BOSH pre-binding
        BoshConnectionConfiguration boshConfiguration = BoshConnectionConfiguration.builder()
/*                .hostname(xmppConfig.getHost())
                .port(xmppConfig.getPort())
                .file(xmppConfig.getHttpBind())
*/               .hostname("localhost")
                .port(5280)
                .file("/http-bind/")
                .wait(65)
                .build();

        xmppClient = new XmppClient(/*xmppConfig.getHost()*/"localhost", boshConfiguration);
        return xmppClient;
	}

	public void unregisterUser(XmppClient xmppClient/*, User authUser, Authentication authentication*/) throws XmppException {
		xmppClient.connect();
    	RegistrationManager registrationManager = xmppClient.getManager(RegistrationManager.class);
    	registrationManager.setEnabled(true);
    	
    	Registration registration = registrationManager.getRegistration();
    	if(registration.isRegistered()) {

    		registrationManager.cancelRegistration();
    	}
	}
	
	public void registerUser(XmppClient xmppClient, User authUser, Authentication authentication) {
        try {
        	System.out.println(".getUsername() " + authUser.getUsername().toString() + " .getPassword " + authUser.getPassword().toString());
        	xmppClient.connect();
        	RegistrationManager registrationManager = xmppClient.getManager(RegistrationManager.class);
        	registrationManager.setEnabled(true);
        	Registration registration = registrationManager.getRegistration();
        	
        	if(!registration.isRegistered()) {
        		registration = Registration.builder()
        				.username(authUser.getUsername().toString())
        				.password(authUser.getPassword().toString())
        				.build();
        		registrationManager.register(registration);
        	}
        	
            xmppClient.login(authUser.getUsername().toString(), authUser.getPassword().toString());

            rocks.xmpp.extensions.httpbind.BoshConnection boshConnection =
                    (rocks.xmpp.extensions.httpbind.BoshConnection) xmppClient.getActiveConnection();

            String sessionId = boshConnection.getSessionId();

            // Detaches the BOSH session, without terminating it.
            long rid = boshConnection.detach();
            //System.out.println("JID: " + xmppClient.getConnectedResource());
            //System.out.println("SID: " + sessionId);
            System.out.println("RID: " + rid);

            XmppUser xmppUser = new XmppUser();
            xmppUser.setUsername((String) authUser.getUsername());
            xmppUser.setJid(xmppClient.getConnectedResource().toString());
            xmppUser.setSid(sessionId);
            xmppUser.setRid(rid);
        } catch (XmppException e) {
            e.printStackTrace();
            throw new XmppAuthenticationException(e.getMessage(), e);
        }
	}
}