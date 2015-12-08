package ciss.in.xmpp;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.extensions.httpbind.BoshConnectionConfiguration;
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;
import ciss.in.Application;
import ciss.in.xmpp.template.XmppAuthenticationException;
import ciss.in.xmpp.template.XmppUser;
import ciss.in.xmpp.template.config.XmppConfig;
import rocks.xmpp.extensions.muc.OccupantEvent;

public class XMPPConnection {

	private XmppConfig xmppConfig;
	
	private XmppClient xmppClient;
	private RegistrationManager registrationManager;
	private Registration registration;

	private BoshConnectionConfiguration boshConfiguration;

	public XMPPConnection() {		
	}
	
	public void setXmppConfig(XmppConfig xmppConfig) {
		this.xmppConfig = Application.xmppConfig;
	}

	public void makeXmppClient() {
		buildBosh();
        xmppClient = new XmppClient(xmppConfig.getHost(), boshConfiguration);
	}
	
	public void setXmppClient(XmppClient xmppClient) {
		this.xmppClient = xmppClient;
	}
	
	public XmppClient getXmppClient() {
		return xmppClient;
	}
	
	public void buildBosh() {
		setXmppConfig(Application.xmppConfig);
		//Authentication and BOSH pre-binding
        boshConfiguration = BoshConnectionConfiguration.builder()
         
        		.hostname(xmppConfig.getHost())
                .port(xmppConfig.getPort())
                .path(xmppConfig.getHttpBind())
                .wait(15)
                .build();		
	}
	
	public void unregisterUser(XmppClient xmppClient, User authUser, Authentication authentication) throws XmppException {
		xmppClient.connect();
		xmppClient.login(authUser.getUsername(), authUser.getPassword());

        registrationManager = xmppClient.getManager(RegistrationManager.class);
    	registrationManager.setEnabled(true);
        registration = registrationManager.getRegistration();
		
		if(registration.isRegistered()) {
    		System.out.println("User " + authUser.getUsername() + " session ended.");
			registrationManager.cancelRegistration();
			xmppClient.close();
    	}
        //xmppClient.close();
	}
	
	public boolean registerUser(XmppClient xmppClient, User authUser) {
		boolean registered = false;
        try {
        	xmppClient.connect();

            registrationManager = xmppClient.getManager(RegistrationManager.class);
        	registrationManager.setEnabled(true);

    		registration = Registration.builder()
    				.username(authUser.getUsername().toString())
    				.password(authUser.getPassword().toString())
    				.build();
        	
        	if(!registration.isRegistered()) {
        		registrationManager.register(registration);
        		System.out.println("Chat User " + registration.getUsername() + " got registered");
        		registered = true;
        	}
        } catch (XmppException e) {
            e.printStackTrace();
            throw new XmppAuthenticationException(e.getMessage(), e);
        }        	
        return registered;
	}
	
	public XmppUser loginUser(XmppClient xmppClient, String username, String password, String domain) {
		XmppUser xmppUser;
        try {
        	//xmppClient.connect();
            xmppClient.login(username, password, domain);

            rocks.xmpp.extensions.httpbind.BoshConnection boshConnection =
                    (rocks.xmpp.extensions.httpbind.BoshConnection) xmppClient.getActiveConnection();

            String sessionId = boshConnection.getSessionId();

            // Detaches the BOSH session, without terminating it.
            long rid = boshConnection.detach();
            //System.out.println("JID: " + xmppClient.getConnectedResource());
            //System.out.println("SID: " + sessionId);
            System.out.println("RID: " + rid);

            xmppUser = new XmppUser();
            xmppUser.setUsername(username);
            xmppUser.setPassword(password);
            xmppUser.setJid(xmppClient.getConnectedResource().toString());
            xmppUser.setSid(sessionId);
            xmppUser.setRid(rid);
        } catch (XmppException e) {
            e.printStackTrace();
            throw new XmppAuthenticationException(e.getMessage(), e);
        }
        return xmppUser;
	}
}