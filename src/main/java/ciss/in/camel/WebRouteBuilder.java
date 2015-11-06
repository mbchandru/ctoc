package ciss.in.camel;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ciss.in.jena.fuseki.processors.QueryProcessor;
import ciss.in.jena.fuseki.processors.UpdateProcessor;

@Component
public class WebRouteBuilder extends RouteBuilder {
	
    final Logger logger = LoggerFactory.getLogger(RouteBuilder.class);

	@Override
	public void configure() throws Exception {
		
		from("direct:channel2").process(new KafkaMessageProcessor())//&serializerClass=kafka.serializer.DefaultEncoder //ciss.in.codecs.CustomMessageEncoder
		.to("kafka:localhost:9092?topic=test&zookeeperHost=localhost&zookeeperPort=2181&groupId=group1&keySerializerClass=kafka.serializer.DefaultEncoder")//.process(new MessageProcessor())
		.to("log:user2")
		.end();/*&serializerClass=kafka.serializer.StringEncoder*/
				
		
		//To Fuseki for load
		from("direct:start-user").process(new UpdateProcessor()).to("restlet:http://localhost:" + "3030" + "/coaf?restletMethod=post" )
		.to("log:user3")
		.end();
		
		//To Fuseki for search
		from("direct:search").process(new QueryProcessor()).to("restlet:http://localhost:" + "3030" + "/coaf?restletMethod=post" )
		.to("log:user4")
		.end();
	}	
}

//. from kafka to suppliers for consumption
//from("direct:channel3").process(new KafkaMessageProcessor())
/*		from("kafka:localhost:9092?topic=test&zookeeperHost=localhost&zookeeperPort=2181&groupId=group1").process(new MessageProcessor())
.to("log:foo")
.end();*/

/*		from("direct:web")//.bean(new MessageProcessor()).bean(new EmailProcessor())
.multicast().parallelProcessing().to("direct:channel1", "direct:channel2", "direct:channel3");*/
//.setBody()
//.simple("Hello World Camel fired at ${header.firedTime}")
//.to("stream:out")
//.setHeader(KafkaConstants.PARTITION_KEY)

/*		from("direct:findOneByQuery")
.to("mongodb:mongoBean?database=kafka&collection=user&operation=findOneByQuery").process(new UserSearch())
.to ("mongodb:mongoBean?database=kafka&collection=user&operation=save")//.process(new SupplierSearchProcessor())
.to("log:resultFindOneByQuery")*/;

//from("direct:channel1")
//.to ("mongodb:mongoBean?database=kafka&collection=user&operation=update")//.process(new SupplierSearchProcessor())
//.to("mongodb:myDb?database=kafka&collection=supplier&operation=findOneByQuery").bean(new EmailProcessor())
//.to("log:user1")
//.end();
//.to store in fuseki server and produce processed data as URI

/*Supplier*/
/*		from("direct:supplier1")
.to ("mongodb:mongoBean?database=kafka&collection=supplier&operation=save").process(new UserSearchProcessor())
.to("mongodb:myDb?database=kafka&collection=user&operation=findOneByQuery").bean(new EmailProcessor())
.to("log:supplier1")
.end();*/
//.to store in fuseki server and produce processed data as URI

/*		from("direct:supplier2").process(new KafkaMessageProcessor())//&serializerClass=kafka.serializer.DefaultEncoder //ciss.in.codecs.CustomMessageEncoder
.to("kafka:localhost:9092?topic=test&zookeeperHost=localhost&zookeeperPort=2181&groupId=group1&keySerializerClass=kafka.serializer.DefaultEncoder")//.process(new MessageProcessor())
.to("log:supplier2")
.end();&serializerClass=kafka.serializer.StringEncoder

from("direct:start-supplier").process(new QueryProcessor()).to("restlet:http://localhost:" + "3030" + "/coaf?restletMethod=post" )
.to("log:supplier3")
.end();*/