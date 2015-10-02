package ciss.in.off.supplier;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository; 
import org.springframework.data.repository.query.Param;


public interface SupplierRepository extends MongoRepository<Supplier, String> {
	Supplier findByUsername(@Param("username") String username);
	Supplier findById(@Param("Id") String id);
	Supplier findByEmail(@Param("email") String email);		
		List<Supplier> findAll();

		Supplier findByResetPasswordToken(String token);
}