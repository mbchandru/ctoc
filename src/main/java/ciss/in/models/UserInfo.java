package ciss.in.models;

//import java.io.Serializable;

import javax.validation.constraints.Size;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class UserInfo {

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
	private String family_name;
    
    @Size(min=2, max=100)
	private String nickName;
    
    @Size(min=2, max=100)
	private String homepage;
    
    @Size(min=2, max=100)
	private String schoolHomepage;
 	
    public UserInfo() {}

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

	public String getFamily_name() {
		return family_name;
	}

	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getSchoolHomepage() {
		return schoolHomepage;
	}

	public void setSchoolHomepage(String schoolHomepage) {
		this.schoolHomepage = schoolHomepage;
	}

	public String getPersonURI() {
		return personURI;
	}

	public void setPersonURI(String personURI) {
		this.personURI = personURI;
	}
}