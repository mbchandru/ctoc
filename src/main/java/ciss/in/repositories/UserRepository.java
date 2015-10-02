package ciss.in.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository; 
import org.springframework.data.repository.query.Param;

import ciss.in.models.User;

public interface UserRepository extends MongoRepository<User, String> {
	User findByUsername(@Param("username") String username);
	User findById(@Param("Id") String id);
	User findByEmail(@Param("email") String email);		
	List<User> findAll();
	User findByResetPasswordToken(String token);
}