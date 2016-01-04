package ciss.in;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.extensions.muc.ChatRoom;
import rocks.xmpp.extensions.muc.ChatService;
import rocks.xmpp.extensions.muc.MultiUserChatManager;
import rocks.xmpp.extensions.muc.model.RoomConfiguration;
import ciss.in.xmpp.XMPPConnection;
import ciss.in.xmpp.template.config.XmppConfig;

@EnableConfigurationProperties(XmppConfig.class)
@SpringBootApplication
//@ImportResource("classpath:mongodb.xml")
public class Application extends SpringBootServletInitializer {

		    @Override
		    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		        return application.sources(Application.class);
		    }
		    
	public static ChatRoom chatRoom;

	public static XMPPConnection xmpp;

	@Autowired
	public static XmppConfig xmppConfig;
	
	public static XmppClient xmppClient;
	
    public static void main(String[] args) throws XmppException, InstantiationException, IllegalAccessException {
        @SuppressWarnings("unused")
		ApplicationContext ctx = SpringApplication.run(Application.class, args);

  		XMPPConnection xmpp = new XMPPConnection();
  		xmpp.setXmppConfig(xmppConfig);
  		
  		xmpp.makeXmppClient();
  		xmppClient = xmpp.getXmppClient();
    	xmppClient.connect();
    	
    	xmppClient.login(xmppConfig.getAdmin(), xmppConfig.getAdminPassword());

/*    	ServiceDiscoveryManager serviceDiscoveryManager = xmppClient.getManager(ServiceDiscoveryManager.class);
    	InfoNode infoNode = serviceDiscoveryManager.discoverInformation(Jid.of("ejabberd@" + xmppConfig.getHost()));*/
    	
    	//Executors.newFixedThreadPool(1).execute(() -> {
        try {

	  		MultiUserChatManager multiUserChatManager = xmppClient.getManager(MultiUserChatManager.class);
	        ChatService chatService = multiUserChatManager.createChatService(Jid.of("conference." + xmppClient.getDomain()));
	        System.out.println("xmppClient.getDomain() "+ xmppClient.getDomain());
	        List<ChatRoom> publicRooms = chatService.discoverRooms();
	        Iterator<ChatRoom> iterator = publicRooms.iterator();
	    	while (iterator.hasNext()) {
	    		if (iterator.next().getRoomInformation().getName().equalsIgnoreCase("freebuys"))
	    		;//System.out.println("room " + iterator.next().getRoomInformation().getName());
	    	}
/*	        RoomConfiguration roomConfiguration = RoomConfiguration.builder().persistent(true).build();
	        ChatRoom cr = new ChatRoom("freebuys", Jid.of("conference." + xmppClient.getDomain()), xmppClient);
	        chatRoom.configure(roomConfiguration);
	        chatRoom.wait(10000);*/
	        chatRoom = chatService.createRoom("freebuys");

	        chatRoom.addOccupantListener(e -> {
	            if (!e.getOccupant().isSelf()) {
	                switch (e.getType()) {
	                    case ENTERED:
	    	                System.out.println(e.getOccupant().getNick() + " has entered the chat session and joined 'FreeBuys' conference room");
	                        break;
	                    case EXITED:
	                        System.out.println(e.getOccupant().getNick() + " has exited the room.");
	                        break;
	                    case KICKED:
	                        System.out.println(e.getOccupant().getNick() + " has been kicked out of the room.");
	                        break;
						case BANNED:
							break;
						case MEMBERSHIP_REVOKED:
							break;
						case NICKNAME_CHANGED:
							break;
						case ROOM_BECAME_MEMBERS_ONLY:
							break;
						case ROOM_DESTROYED:
							break;
						case STATUS_CHANGED:
			                System.out.println(e.getOccupant() + " had changed status");
							break;
						case SYSTEM_SHUTDOWN:
							break;
						default:
							break;
	                }
	            }
	        });
	        chatRoom.enter(xmppConfig.getAdmin());
            chatRoom.sendMessage("Hello All! This is " + xmppConfig.getAdmin());
            RoomConfiguration roomConfiguration = RoomConfiguration.builder().persistent(true).build();
	        //ChatRoom cr = new ChatRoom("freebuys", Jid.of("conference." + xmppClient.getDomain()), xmppClient);
	        chatRoom.configure(roomConfiguration);
		} catch (Exception e) {
			e.printStackTrace();
		}
        //});
    }
    
	@Autowired
	void setConfigurationProjectProperties(XmppConfig xmppConfig) {
		Application.xmppConfig = xmppConfig;
	}
}