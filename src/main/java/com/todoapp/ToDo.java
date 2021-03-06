package com.todoapp;
 
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity
public class ToDo {
	@Id
	private int id;
    private String title;
    private boolean done;
    private Date createdOn = new Date();
    @Reference
    private User user;
    @Embedded
    private List<Task> taskList;
    @Reference
    private Set<User> userSet;
    private BigDecimal progress;
    
    public ToDo(){
    	
    }
    
    public int getId(){
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
    
    public void setId(int id){
    	this.id = id;
    }
    
    public void setTaskList(List<Task> taskList){
    	this.taskList = taskList;
    }
    
    public List<Task> getTaskList(){
    	return taskList;
    }
    
    public void setUserSet(Set<User> userSet){
    	this.userSet = userSet;
    }
    
    public Set<User> getUserSet(){
    	return userSet;
    }
    
    public void setProgress(BigDecimal progress){
    	this.progress = progress;
    }
    
    public BigDecimal getProgress(){
    	return progress;
    }
}