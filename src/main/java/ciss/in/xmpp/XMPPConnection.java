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

	//@Autowired
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
		//return xmppClient;
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
                .wait(65)
                .build();		
	}
	
	public void unregisterUser(XmppClient xmppClient, User authUser, Authentication authentication) throws XmppException {
    	xmppClient.connect();
        xmppClient.login(authUser.getUsername(), authUser.getPassword());
		
        registrationManager = xmppClient.getManager(RegistrationManager.class);
    	registrationManager.setEnabled(true);
    	registrationManager.cancelRegistration();
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
	
	public void loginUser(XmppClient xmppClient, User authUser, Authentication authentication) {
        try {
            xmppClient.login(authUser.getUsername().toString(), authUser.getPassword().toString());
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
	                Application.chatRoom.sendMessage("Hello All! This is " + e.getOccupant());
	            }
	        });
            Application.chatRoom.sendMessage("Hello All! " + authUser.getUsername().toString());
	        
	        
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