package com.todoapp;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

@Entity
public class User {
	@Id
    private ObjectId id;
    private String name;
    @Indexed(unique = true)
    private String login;
    private String password;
    private Date createdOn = new Date();
    
    public User(){
    	
    }
    
    public ObjectId getId(){
    	return id;
    }
 
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password){
    	this.password = password;
    }
    
    public Date getCreatedOn() {
        return createdOn;
    }
    
    public String getLogin(){
    	return login;
    }
    
    public void setLogin(String login){
    	this.login = login;
    }
}
