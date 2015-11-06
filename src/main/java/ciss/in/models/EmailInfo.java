package ciss.in.models;

import java.io.Serializable;
import java.util.ArrayList;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.mongodb.morphia.annotations.Entity;
import org.springframework.data.annotation.Id;

@Entity
public class EmailInfo implements Serializable {
  
	private static final long serialVersionUID = -5329477561242064847L;

  @Id
  private String id;
  
  //@Size(min=6, max=15, message="Name shall be min 6 character")
  //@Pattern(regexp="^[a-z0-9_-]{3,8}$",
  //message="Please enter valid Name, starting with an alphabet, no special characters allowed")
  private String name;
  
/*  @Size(min=6, max=15, message="Company Name shall be min 6 character")
  private String companyName;*/

  @Size(min=3, max=85, message="Subject shall be min 3 character")
  private String subject;

  @Size(min=3, max=85, message="Message shall be min 3 character")
  private String message;

  
  @NotNull
  @Size(min=10, max=10)
  @Pattern(regexp="^[789]\\d{9}$",
  message="Please enter valid phone number starting with 7, 8 or 9")
  private String number;
  
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

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

/*public String getCompanyName() {
	return companyName;
}

public void setCompanyName(String companyName) {
	this.companyName = companyName;
}*/

public String getSubject() {
	return subject;
}

public void setSubject(String subject) {
	this.subject = subject;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

public String getNumber() {
	return number;
}

public void setNumber(String number) {
	this.number = number;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public ArrayList<String> getProducts() {
	return products;
}

public void setProducts(ArrayList<String> products) {
	this.products = products;
}

public ArrayList<String> getTransactionTypes() {
	return transactionTypes;
}

public void setTransactionTypes(ArrayList<String> transactionTypes) {
	this.transactionTypes = transactionTypes;
}

public String[][] getProductTransactions() {
	return productTransactions;
}

public void setProductTransactions(String[][] productTransactions) {
	this.productTransactions = productTransactions;
}

private ArrayList<String> products;
private ArrayList<String> transactionTypes;
private String[][] productTransactions;
}