package com.todoapp;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class QuizzCategory {
	private ObjectId objectId;
	@Id
	private int id;
	private String name;
	private Date createdOn = new Date();
	@Embedded
	private List<QuizzQuestions> questionsList;
	
	public QuizzCategory(){
		questionsList = new LinkedList<QuizzQuestions>();
	}
	
	public int getId(){
    	return id;
    }
	
	public void setId(int id){
		this.id = id;
	}
 
    public String getName() {
        return name;
    }
 
    public Date getCreatedOn() {
        return createdOn;
    }
    
    public List<QuizzQuestions> getQuizzQuestionsList(){
    	return questionsList;
    }
}
