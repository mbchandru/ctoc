package ciss.in.off.consumer;

import org.springframework.context.annotation.Configuration;

import ciss.in.models.UserInfo;


@Configuration
public interface ConsumerMessageService {
	public String sendMessage(UserInfo msg);
}