package ciss.in.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

import org.mongodb.morphia.annotations.Entity;
//import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

@Entity
public class User extends Person implements Serializable {
  
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
/*  @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
	        +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
	        +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	             message="Please enter valid email address")
  private String email;*/
  
  private int role;
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  private String resetPasswordToken;
  
  private Date resetPasswordExpires;
  
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
	
	private ArrayList<MultipartFile> imageFiles;
	
	public ArrayList<MultipartFile> getImageFiles() {
		return imageFiles;
	}

	public void setImageFiles(ArrayList<MultipartFile> imageFiles) {
		this.imageFiles = imageFiles;
	}
	
	public ArrayList<String> getTransactionTypes() {
		return transactionTypes;
	}

	public void setTransactionTypes(ArrayList<String> transactionTypes) {
		this.transactionTypes = transactionTypes;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getProductSearch() {
		return productSearch;
	}

	public void setProductSearch(String productSearch) {
		this.productSearch = productSearch;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	private ArrayList<String> transactionTypes;
	
	private String productCategory;
	
	private String productSearch;
	
	private String userType;
}