package ciss.in.off;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ciss.in.models.UserInfo;

public class KafkaMsgConsumer {

    final Logger logger = LoggerFactory.getLogger(KafkaMsgConsumer.class);
	@Autowired
	CamelContext camelContext;
	
    public void getMsg() {
    	Endpoint controller = camelContext.getEndpoint("kafka:localhost:9092?topic=test&zookeeperHost=localhost&zookeeperPort=2181");//&groupId=group1&serializerClass=kafka.serializer.StringEncoder
    	ConsumerTemplate consumerTemplate = camelContext.createConsumerTemplate();
    	UserInfo receiveMsg = (UserInfo) consumerTemplate.receive(controller);
    	System.out.println(receiveMsg.getFullName());
    }
}