package com.todoapp;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class Sequence {
	@Id
	private ObjectId id;
	private String className;
	private int start;
	
	public Sequence(){
		
	}
	
	public Sequence(String className, int start){
		this.className = className;
		this.start = start;
	}
	
	public int getStart(){
		return start;
	}
	
	public String getClassName(){
		return className;
	}
	
	public int getNextValue(){
		int value = start;
		start ++;
		return value;
	}
}
