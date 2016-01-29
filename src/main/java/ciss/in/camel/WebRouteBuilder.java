package ciss.in.camel;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ciss.in.Application;
import ciss.in.jena.fuseki.processors.QueryProcessor;
import ciss.in.jena.fuseki.processors.UpdateProcessor;

@Component
public class WebRouteBuilder extends RouteBuilder {
	
    final Logger logger = LoggerFactory.getLogger(RouteBuilder.class);
    
    public WebRouteBuilder() {
    	
    }
	@Override
	public void configure() throws Exception {
	
		from("direct:channel2").process(new KafkaMessageProcessor())//&serializerClass=kafka.serializer.DefaultEncoder //ciss.in.codecs.CustomMessageEncoder
		.to("kafka:" + Application.xmppConfig.getKafkaHost() + "?topic=test&zookeeperHost=" + Application.xmppConfig.getZookeeperHost() + "&zookeeperPort=" + Application.xmppConfig.getZookeeperPort() + "&groupId=group1&keySerializerClass=kafka.serializer.DefaultEncoder")//.process(new MessageProcessor())
		.to("log:user2")
		.end();/*&serializerClass=kafka.serializer.StringEncoder*/
		String service;		
		if (Application.xmppConfig.getFusekiPort().isEmpty())
			service = "";
		else
			service = ":3030";
		
		//To Fuseki for load
		from("direct:start-user").process(new UpdateProcessor()).to("restlet:http://" + Application.xmppConfig.getFusekiHost() + service + "/coaf?restletMethod=post" )
		.to("log:user3")
		.end();
		
		//To Fuseki for search
		from("direct:search").process(new QueryProcessor()).to("restlet:http://" + Application.xmppConfig.getFusekiHost() + service + "/coaf?restletMethod=post" )
		.to("log:user4")
		.end();
		
		//from user to xmpp server
		from("direct:xmpp-start").process(new XmppProcessor())//.to("xmpp://superman@jabber.org/?password=secret&room=krypton@conference.jabber.org")
		.to("log:user5")
		.end();
	}	
}