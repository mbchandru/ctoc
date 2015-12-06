package ciss.in.camel;

//import java.util.HashMap;
//import java.util.Map;

//import org.apache.camel.CamelContext;




import org.apache.camel.Exchange;
//import org.apache.camel.Message;
import org.apache.camel.Processor;
//import org.apache.camel.component.xmpp.XmppEndpoint;
//import org.jivesoftware.smack.ConnectionConfiguration;
//import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
//import org.jivesoftware.smack.XMPPConnection;
//import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;


@Configuration
public class XmppProcessor implements Processor {

    final Logger logger = LoggerFactory.getLogger(XmppProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		//String body = exchange.getIn().getBody(String.class);

		//CamelContext context = exchange.getContext();
		
		/*    	Map<String, Object> ns = new HashMap<String, Object>();
    	ns = exchange.getIn().getHeaders();

    	String host = (String) ns.get("host");
		Integer port = (Integer) ns.get("port");
		String user = (String) ns.get("user");
		String password = (String) ns.get("password");
		String room = (String) ns.get("room");*/
    	
		//xmpp://superman@jabber.org/?password=secret&room=krypton@conference.jabber.org
		//Endpoint xmppPoint = context.getEndpoint("xmpp://" + user + "@" + host + ":" + port + "/?password=" + password + "&room=" + room);

/*		XmppEndpoint xmppPoint = new XmppEndpoint();
		xmppPoint.setHost(host);
		xmppPoint.setPort(port.intValue());
		xmppPoint.setUser(user);
		xmppPoint.setPassword(password);
		xmppPoint.setRoom(room);*/
		//System.out.print(" host "+ host +" port "+ port +" user "+ user +" password "+ password +" room "+ room);
/*		ConnectionConfiguration connectionConfig = new ConnectionConfiguration(host,port);
		connectionConfig.setSecurityMode(SecurityMode.disabled);
		XMPPConnection mConnection=new XMPPConnection(connectionConfig);
		    mConnection.connect();
		    mConnection.login(user,password);
		 // Start a new conversation with John Doe and send him a message.
		    Chat chat = ChatManager.getInstanceFor(con).createChat("jdoe@igniterealtime.org", new MessageListener() {
		        public void processMessage(Chat chat, Message message) {
		            // Print out any messages we get back to standard out.
		            System.out.println("Received message: " + message);
		        }
		    });
		    chat.sendMessage("Howdy!");
		    // Disconnect from the server
		    mConnection.disconnect();*/

		    
		//XMPPConnection xmppConnect = xmppPoint.createConnection();
		//xmppConnect.login(user, password);
		//xmppConnect.sendPacket(packet);
		
		// create an exchange with a normal body and attachment to be produced as email
		//Endpoint endpoint = context.getEndpoint("smtps://mbchandru@careerenabler.in?host=smtp.zoho.com&port=465&from=mbchandru@careerenabler.in&password=saibaba123#");

/*		Exchange exchangeOut = xmppPoint.createExchange();
		Message out = exchangeOut.getIn();
		out.setBody(body);*/

		// create a producer that can produce the exchange (= send the mail)
		//Producer producer = xmppPoint.createProducer();

		//producer.start();

		//producer.process(exchangeOut);
	}
}