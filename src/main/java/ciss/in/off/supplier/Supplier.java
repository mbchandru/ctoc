package ciss.in.off.supplier;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

import org.mongodb.morphia.annotations.Entity;
//import org.springframework.data.annotation.Id;

@Entity
public class Supplier implements Serializable {
  
	private static final long serialVersionUID = -5329477561242064847L;

  //@Id
  private String id;
  
  //@Size(min=6, max=15, message="Username shall be min 6 character")
  //@Pattern(regexp="^[a-z0-9_-]{3,8}$",
  //message="Please enter valid username, starting with an alphabet, no special characters allowed, and checkbox - 'I'm not a robot' - not selected")
  private String username;
  
  @Size(min=6, max=15, message="Username shall be min 6 character")
  private String password;

  @NotNull
  @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
	        +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
	        +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	             message="Please enter valid email address")
  private String email;
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  private String resetPasswordToken;
  
  private Date resetPasswordExpires;
  
  public Supplier(){}
  
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
}