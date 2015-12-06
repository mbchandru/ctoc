package ciss.in.zxing;

/*import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

import rocks.xmpp.core.XmppException;*/
import rocks.xmpp.extensions.muc.ChatRoom;

public class Test {
	public static ChatRoom chatRoom;
	
	
    /*public static void main(String[] args) throws XmppException, IOException {


    	XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
       			.setHost("localhost")
				.setPort(5222)
				.setServiceName("localhost")
				//.setUsernameAndPassword(xmppUser.getUsername(), xmppUser.getPassword())
				//.setResource("freebuys@conference")
				//.setSocketFactory(SSLSocketFactory.getDefault())
				//.setConnectTimeout(10000)
				//.setSendPresence(true)
				//.setDebuggerEnabled(true)
				//.setCompressionEnabled(false)
				.setSecurityMode(SecurityMode.disabled)
				.build();
       	
       	AbstractXMPPConnection connection = new XMPPTCPConnection(config);
       	// Connect to the server
       	try {
       		connection.setPacketReplyTimeout(10000);
			connection.connect();

			//SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
			//SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
	       	connection.login("admin", "admin");
	       	ChatManager chatmanager = ChatManager.getInstanceFor(connection);
            Chat newChat = chatmanager.createChat("admin@localhost");

            newChat.sendMessage("Goodbye World! Hi Hi");
            connection.disconnect();
		} catch (SmackException | XMPPException e) {
			e.printStackTrace();
		}
    }*/
}