package ciss.in.models;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

import org.mongodb.morphia.annotations.Entity;
//import org.springframework.data.annotation.Id;

@Entity
public class User implements Serializable {
  
	private static final long serialVersionUID = -5329477561242064847L;

  //@Id
  private String id;
  
  @Size(min=6, max=15, message="Username shall be min 6 character")
  @Pattern(regexp="^[a-z0-9_-]{3,8}$",
  message="Please enter valid username, starting with an alphabet, no special characters allowed, and checkbox - 'I'm not a robot' - not selected")
  private String username;
  
  @Size(min=6, max=15, message="Username shall be min 6 character")
  private String password;

  @NotNull
  @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
	        +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
	        +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	             message="Please enter valid email address")
  private String email;
  
  private int role;
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  private String resetPasswordToken;
  
  private Date resetPasswordExpires;
  
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

  public User(){}
  
  public String getUsername() {
	    return username;
	  }
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public Date getResetPasswordExpires() {
		return resetPasswordExpires;
	}

	public void setResetPasswordExpires(Date resetPasswordExpires) {
		this.resetPasswordExpires = resetPasswordExpires;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
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