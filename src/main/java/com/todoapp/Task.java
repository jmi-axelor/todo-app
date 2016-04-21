package com.todoapp;
 
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Reference;

@Embedded
public class Task {
	private int id;
    private String title;
    private boolean done;
    private BigDecimal progress;
    private Date createdOn = new Date();
    @Reference
    private User user;
    @Embedded
    private List<Task> taskList;
    private int parentTodoId;
    private int parentTaskId;
    
    public Task(){
    	
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
    
    public void setProgress(BigDecimal progress){
    	this.progress = progress;
    }
    
    public BigDecimal getProgress(){
    	return progress;
    }
    
    public void setTaskList(List<Task> taskList){
    	this.taskList = taskList;
    }
    
    public List<Task> getTaskList(){
    	return taskList;
    }
    
    public void setParentTodoId(int parentTodoId){
    	this.parentTodoId = parentTodoId;
    }
    
    public int getParentTodoId(){
    	return this.parentTodoId;
    }
    
    public void setParentTaskId(int parentTaskId){
    	this.parentTaskId = parentTaskId;
    }
    
    public int getParentTaskId(){
    	return this.parentTaskId;
    }
}