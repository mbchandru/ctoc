package ciss.in.xmpp;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.extensions.httpbind.BoshConnectionConfiguration;
import rocks.xmpp.extensions.muc.OccupantEvent;
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;
import ciss.in.Application;
import ciss.in.xmpp.template.XmppAuthenticationException;
import ciss.in.xmpp.template.XmppUser;
import ciss.in.xmpp.template.config.XmppConfig;

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
                .wait(10)
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
        		System.out.println("Chat User " + registration.getUsername() + " got registered and joined 'FreeBuys' conference room");
        		registered = true;
        	}
        } catch (XmppException e) {
            e.printStackTrace();
            throw new XmppAuthenticationException(e.getMessage(), e);
        }        	
        return registered;
	}
	
	public XmppUser loginUser(XmppClient xmppClient, User authUser, Authentication authentication, String domain) {
		XmppUser xmppUser;
        try {
            xmppClient.login(authUser.getUsername().toString(), authUser.getPassword().toString(), domain);
            rocks.xmpp.extensions.httpbind.BoshConnection boshConnection =
                    (rocks.xmpp.extensions.httpbind.BoshConnection) xmppClient.getActiveConnection();

            String sessionId = boshConnection.getSessionId();

            // Detaches the BOSH session, without terminating it.
            long rid = boshConnection.detach();
            //System.out.println("JID: " + xmppClient.getConnectedResource());
            //System.out.println("SID: " + sessionId);
            System.out.println("RID: " + rid);

            //Enter chat room
            Application.chatRoom.addOccupantListener(e -> {
	            if (e.getType() == OccupantEvent.Type.ENTERED) {
	                System.out.println(e.getOccupant() + " has entered the room");
	                //Application.chatRoom.sendMessage("Hello All! This is " + e.getOccupant() + " joined");
	            }
	        });
	        
            xmppUser = new XmppUser();
            xmppUser.setUsername(authUser.getUsername());
            xmppUser.setPassword(authUser.getPassword());
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