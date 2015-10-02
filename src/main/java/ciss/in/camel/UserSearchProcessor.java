package ciss.in.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ciss.in.models.UserInfo;

@Component
public class UserSearchProcessor implements Processor {

    final Logger logger = LoggerFactory.getLogger(UserSearchProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		UserInfo webMsg = exchange.getIn().getBody(UserInfo.class);

		//get the field from the user-data for search
		
        exchange.getOut().setBody(webMsg);
	}
}