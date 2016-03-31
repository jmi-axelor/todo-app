package com.todoapp;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class Sequence {
	@Id
	private ObjectId id;
	private String name;
	private int start;
	
	public Sequence(){
		
	}
	
	public Sequence(String name, int start){
		this.name = name;
		this.start = start;
	}
	
	public int getStart(){
		return start;
	}
	
	public String getName(){
		return name;
	}
	
	public int getNextValue(){
		int value = start;
		start ++;
		return value;
	}
}
