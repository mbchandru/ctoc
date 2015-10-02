package ciss.in.off.consumer;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
//import org.apache.camel.Endpoint;
//import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ciss.in.models.UserInfo;
import ciss.in.off.codecs.CustomMessageDecoder;

@Component
public class SupplierMessageProcessor implements Processor {

    final Logger logger = LoggerFactory.getLogger(SupplierMessageProcessor.class);

	@Autowired
	CamelContext camelContext;
	
	@Autowired
	ConsumerTemplate consumerTemplate;
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		String result;
		
		//Endpoint controller1 = camelContext.getEndpoint("kafka:localhost:9092?topic=test&zookeeperHost=localhost&zookeeperPort=2181&groupId=group1");
    	//consumerTemplate = camelContext.createConsumerTemplate();
    	//Exchange exchangeE = consumerTemplate.receive("kafka:localhost:9092?topic=test&zookeeperHost=localhost&zookeeperPort=2181&groupId=group1");
		
		byte[] bytes = exchange.getIn().getBody(byte[].class);
		CustomMessageDecoder decoder = new CustomMessageDecoder(null);
		UserInfo webMsg = decoder.fromBytes(bytes);
		
		//UserInfo webMsg = exchange.getIn().getBody(UserInfo.class);
    	System.out.println("webMsg "+ webMsg);
		
        //UserInfo entry = exchange.getIn().getBody(UserInfo.class);
        //String entryString = exchange.getIn().getBody(String.class);

        //ConsumerTemplate consumerTemplate = camelContext.createConsumerTemplate();
        //consumerTemplate.doneUoW(exchange);
        
/*        logger.debug("In MessageProcessor.  collegeName='{}'", 
                     new Object[] { entry.getCollegeName()
					        	  });*/
    	//System.out.println("entryString "+ entryString);
        if (webMsg.getFullName() == null) {
                result = "Please upload student details";
                //send email to College to upload student details
        }
        else { 
        	result = "Please sign contract";
            //send email to College to sign contract
        }
        exchange.getOut().setBody(result);
	}
}