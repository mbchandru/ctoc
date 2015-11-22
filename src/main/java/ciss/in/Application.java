package ciss.in;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.extensions.muc.ChatRoom;
import rocks.xmpp.extensions.muc.ChatService;
import rocks.xmpp.extensions.muc.MultiUserChatManager;
import rocks.xmpp.extensions.muc.OccupantEvent;
import ciss.in.xmpp.XMPPConnection;
import ciss.in.xmpp.template.config.XmppConfig;

@EnableConfigurationProperties(XmppConfig.class)
@SpringBootApplication
@ImportResource("classpath:mongodb.xml")
public class Application /*extends SpringBootServletInitializer*/ {
	public static ChatRoom chatRoom;
	
	@Autowired
	public static XmppConfig xmppConfig;
	
	private static XmppClient xmppClient;
	
    public static void main(String[] args) {
        @SuppressWarnings("unused")
		ApplicationContext ctx = SpringApplication.run(Application.class, args);

  		XMPPConnection xmpp = new XMPPConnection();
  		xmpp.setXmppConfig(xmppConfig);
  		
  		xmpp.makeXmppClient();
  		xmppClient = xmpp.getXmppClient();
  		
		Executors.newFixedThreadPool(1).execute(() -> {
        try {
	  		xmppClient.connect();
	  		xmppClient.login("admin", "admin", "localhost");

	  		MultiUserChatManager multiUserChatManager = xmppClient.getManager(MultiUserChatManager.class);
	        ChatService chatService = multiUserChatManager.createChatService(Jid.of("conference." + xmppClient.getDomain()));
	        chatRoom = chatService.createRoom("FreeBuys");
	        
	        chatRoom.addOccupantListener(e -> {
	            if (e.getType() == OccupantEvent.Type.ENTERED) {
	                System.out.println(e.getOccupant() + " has entered the room");
	                chatRoom.sendMessage("Hello All! This is " + e.getOccupant());
	            }
	        });

			chatRoom.enter("admin");
	        chatRoom.sendMessage("Hello All!, This is CXC Admin");
	        
		} catch (XmppException e) {
			e.printStackTrace();
		}
        });
    }
    
	@Autowired
	void setConfigurationProjectProperties(XmppConfig xmppConfig) {
		Application.xmppConfig = xmppConfig;
	}
}