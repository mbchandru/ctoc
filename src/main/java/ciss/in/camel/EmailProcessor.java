package ciss.in.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailProcessor implements Processor {

    final Logger logger = LoggerFactory.getLogger(EmailProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		String body = exchange.getIn().getBody(String.class);

		CamelContext context = exchange.getContext();

		// create an exchange with a normal body and attachment to be produced as email
		Endpoint endpoint = context.getEndpoint("smtps://mbchandru@careerenabler.in?host=smtp.zoho.com&port=465&from=mbchandru@careerenabler.in&password=saibaba123#");

		Exchange exchangeOut = endpoint.createExchange();
		Message out = exchangeOut.getIn();
		out.setHeader("subject", "Hi, eMail from CareerEnabler.in, for your action please");
		out.setBody(body);

		// create a producer that can produce the exchange (= send the mail)
		Producer producer = endpoint.createProducer();

		producer.start();

		producer.process(exchangeOut);
	}
}