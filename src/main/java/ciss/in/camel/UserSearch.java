package ciss.in.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserSearch implements Processor {

    final Logger logger = LoggerFactory.getLogger(UserSearch.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		ciss.in.models.User webMsg = exchange.getIn().getBody(ciss.in.models.User.class);
		System.out.println("webMsg " + webMsg);
		//get the field from the user-data for search
		
        exchange.getOut().setBody(webMsg);
	}
}