package ciss.in.off.supplier;

//import java.io.Serializable;

import javax.validation.constraints.Size;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class SupplierInfo {

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;

	@Id private String id;

    @Size(min=2, max=100)
	private String personURI;
	
    @Size(min=2, max=100)
 	private String fullName;
    
    @Size(min=2, max=100)
	private String title;
    
    @Size(min=2, max=100)
	private String givenname;
        
    @Size(min=2, max=100)
	private String homepage;
     	
    public SupplierInfo() {}

    public String getId() {
    	return id;
    }
    
    public void setId(String id) {
    	this.id = id;
    }

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGivenname() {
		return givenname;
	}

	public void setGivenname(String givenname) {
		this.givenname = givenname;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getPersonURI() {
		return personURI;
	}

	public void setPersonURI(String personURI) {
		this.personURI = personURI;
	}
}