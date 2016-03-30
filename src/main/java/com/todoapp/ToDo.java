package com.todoapp;
 
import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity
public class ToDo {
	@Id
    private ObjectId id;
    private String title;
    private boolean done;
    private Date createdOn = new Date();
    @Reference
    private User user;
    
    public ToDo(){
    	
    }
    
    public ObjectId getId(){
    	return id;
    }
 
    public String getTitle() {
        return title;
    }
 
    public boolean getDone() {
        return done;
    }
    
    public void setDone(boolean done) {
        this.done = done;
    }
 
    public Date getCreatedOn() {
        return createdOn;
    }
    
    public User getUser() {
    	return user;
    }
}