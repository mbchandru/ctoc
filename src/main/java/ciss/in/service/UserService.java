package ciss.in.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ciss.in.models.User;

import ciss.in.repositories.UserRepository;

public class UserService {

	static final Logger logger = LoggerFactory.getLogger(UserService.class);

	private UserRepository repos;
	
	public UserService(UserRepository repos) {
		this.repos = repos;
	}
	
	public UserService() {
	}

	public void create(User user) {
		repos.save(user);
	}
	
	public void update(User user) {
		repos.save(user);
	}
	
	public User read(String username) {
		return repos.findByUsername(username);
	}
	
	public void delete(User user) {
		repos.delete(user);
	}		

	public List<User> list() {
		return repos.findAll();
	}	
}