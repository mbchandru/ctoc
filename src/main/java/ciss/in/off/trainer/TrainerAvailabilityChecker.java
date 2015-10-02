/*package com.workflow.trainer;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

import com.model.Contact;
import com.repository.ContactRepository;
import com.repository.TrainerRepository;
import com.workflow.courseschedule.TrainingCourseSchedule;

public class TrainerAvailabilityChecker implements Processor {
	
	@Autowired
	ContactRepository trainerRepository;
	
	@Autowired
	TrainerRepository trainerCoursesRepository;

	List<Contact> courseByTrainers;
	
	public void process(Exchange exchange) throws Exception {		
		String certificateCourse = exchange.getIn().getBody(String.class);
		System.out.println("Check availability of trainer for " + certificateCourse);
		List<String> courses = trainerCoursesRepository.findByTrainingSubjects(certificateCourse ); 

		if (courses == null) {
			throw new TrainerNotAvailableException(exchange, "No trainer for " + certificateCourse);
		}
		else {
			List<Contact> courseTrainers = trainerRepository.findByContactType("trainer");
			for (int i = 0; i <= courseTrainers.size(); i++) {
				List<String> coursesT = trainerCoursesRepository.findByTrainingSubjects(certificateCourse);
				for (int j = 0; j <= courses.size(); j++) {
					if (coursesT.get(j).equals(certificateCourse)) {
						System.out.print("Found Trainers: " );
						courseByTrainers.add(courseTrainers.get(i));
					}
				}
			}
			
		}
		exchange.getOut().setBody(new TrainingCourseSchedule(courseByTrainers, certificateCourse), TrainingCourseSchedule.class);
	}
}*/