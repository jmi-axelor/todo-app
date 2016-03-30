package com.todoapp;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class User {
	@Id
    private ObjectId id;
    private String name;
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
    
    public Date getCreatedOn() {
        return createdOn;
    }
}
