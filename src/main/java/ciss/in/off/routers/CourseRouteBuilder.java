/*package ciss.in.routers;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.workflow.courseschedule.TrainingScheduler;
import com.workflow.trainer.TrainerAvailabilityChecker;
import com.workflow.trainer.TrainerNotAvailableException;

@Configuration
public class CourseRouteBuilder extends RouteBuilder {
	
    final Logger logger = LoggerFactory.getLogger(CourseRouteBuilder.class);
    
    @Autowired
    TrainingScheduler scheduleCourse;
    
	@Override
	public void configure() throws Exception {

		//Trigger this when a certificate is added in StudentPref-class certPref list attribute
		from("direct:courseName")
		   .onException(TrainerNotAvailableException.class)
		   .handled(true)
		   .transform(constant("No trainer available exception"))
		   .to("stream:out")
		   .end()
		.process(new TrainerAvailabilityChecker())					
		.to("bean:scheduleCourse?method=schedule")
		.setBody()
		.simple("courseName fired at ${header.firedTime}")
		.to("stream:out");
	}	
}*/