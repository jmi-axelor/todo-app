package com.todoapp;

import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class QuizzQuestions {
	private String name;
	private Date createdOn = new Date();
	private String question;
	private List<String> propositionsList;
	private String response;
	private boolean done;
	
	public QuizzQuestions(){
		this.done = false;
	}
	
    public String getName() {
        return name;
    }
 
    public Date getCreatedOn() {
        return createdOn;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public List<String> getPropositionsList() {
        return propositionsList;
    }
    
    public String getResponse() {
        return response;
    }
    
    public boolean getDone(){
    	return done;
    }
    
    public void setDone(boolean done){
    	this.done = done;
    }
    
    public void setPropositionsList(List<String> propositionsList){
    	this.propositionsList = propositionsList;
    }
    
}
