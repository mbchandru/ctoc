package ciss.in.off.supplier;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SupplierService {

	static final Logger logger = LoggerFactory.getLogger(SupplierService.class);

	private SupplierRepository repos;
	
	public SupplierService(SupplierRepository repos) {
		this.repos = repos;
	}
	
	public SupplierService() {
	}

	public void create(Supplier supplier) {
		repos.save(supplier);
	}
	
	public void update(Supplier supplier) {
		repos.save(supplier);
	}
	
	public Supplier read(String username) {
		return repos.findByUsername(username);
	}
	
	public void delete(Supplier supplier) {
		repos.delete(supplier);
	}		

	public List<Supplier> list() {
		return repos.findAll();
	}	
}