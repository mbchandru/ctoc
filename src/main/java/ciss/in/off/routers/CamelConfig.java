package ciss.in.off.routers;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;




import ciss.in.models.UserInfo;
import ciss.in.off.consumer.ConsumerMessageService;


public class CamelConfig {
 
	@Autowired
	static
	CamelContext camelContext;
	
	ConsumerMessageService msgService;
	
    
    @SuppressWarnings("unused")
	public static void  startCamelProducerConsumer() {
    	
/*    	Endpoint controller1 = camelContext.getEndpoint("direct:web");
    	producerTemplate = camelContext.createProducerTemplate();*/
    	
    	Endpoint controller = camelContext.getEndpoint("kafka:localhost:9092?topic=test&zookeeperHost=localhost&zookeeperPort=2181&groupId=group1&serializerClass=kafka.serializer.StringEncoder");
    	ConsumerTemplate consumerTemplate = camelContext.createConsumerTemplate();
    	Exchange exchange = consumerTemplate.receive(controller);
    	UserInfo supplierData = exchange.getIn().getBody(UserInfo.class);
    	
    	String result = null;
    	System.out.println("supplierData "+ supplierData);
        if (supplierData.getFullName() == null) {
        	result = "Please upload student details";
                //send email to College to upload student details
        }
        else { 
        	result = "Please sign contract";
            //send email to College to sign contract
        }
    }
}