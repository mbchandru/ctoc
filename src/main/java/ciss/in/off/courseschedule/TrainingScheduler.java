/*package com.workflow.courseschedule;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import com.model.Contact;

@Configuration
public class TrainingScheduler {
	
	public TrainingCourseSchedule schedule(TrainingCourseSchedule courseSchedule) {
		
		String course = courseSchedule.getCourse();
		List<Contact> trainers = courseSchedule.getTrainer();
		System.out.println("Schedule " + course + ", trainer is " + trainers);
		courseSchedule.setTrainingDate(new Date());
		return courseSchedule; 
	}
}*/