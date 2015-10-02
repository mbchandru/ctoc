package ciss.in.camel;

import java.io.ByteArrayOutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//import ciss.in.codecs.CustomMessageEncoder;
//import ciss.in.jena.RDFCrud;
//import ciss.in.models.UserInfo;

@Component
public class KafkaMessageProcessor implements Processor {

    final Logger logger = LoggerFactory.getLogger(KafkaMessageProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		ByteArrayOutputStream byteMsg = exchange.getIn().getBody(ByteArrayOutputStream.class);
    	byte[] bytes = byteMsg.toByteArray();

    	//CustomMessageEncoder encoder = new CustomMessageEncoder(null);
    	//byte[] bytes = encoder.toBytes(newMsg);
    	
        exchange.getOut().setBody(bytes);
	}
}