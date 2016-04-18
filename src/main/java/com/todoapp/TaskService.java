package com.todoapp;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class TaskService {
	
	private final Datastore ds;
    public SequenceService sequenceService; 
    
    public TaskService(MongoClient mongoClient, String dbName, Morphia morphia, SequenceService sequenceService) {
        morphia.map(Task.class);
        ds = morphia.createDatastore(mongoClient, dbName);
        this.sequenceService = sequenceService;
    }
    public Task find(int todoId, int taskId){
    	ToDo todo = ds.find(ToDo.class, "_id", todoId).get();
    	return this.find(todo, taskId);
    }
    
    public Task find(ToDo todo, int taskId){
    	Task taskFound = null;
    	if(todo.getTaskList() == null){
    		return null;
    	}
    	for (Task task : todo.getTaskList()) {
			if(task.getId() == taskId){
				return task;
			}
		}
    	for (Task task : todo.getTaskList()) {
    		taskFound = this.findTaskInTask(task, taskId);
    		if(taskFound != null){
    			return taskFound;
    		}
    	}
    	return null;
    }
    
    public Task findTaskInTask(Task task, int taskId){
    	Task taskFound = null;
    	if(task.getTaskList() == null){
    		return null;
    	}
    	for (Task taskIt : task.getTaskList()) {
			if(taskIt.getId() == taskId){
				return taskIt;
			}
		}
    	for (Task taskIt : task.getTaskList()) {
			taskFound = this.findTaskInTask(taskIt, taskId);
			if(taskFound != null){
    			return taskFound;
    		}
		}
    	return null;
    }
    
    public BigDecimal computeProgress(Task task){
    	BigDecimal progress = BigDecimal.ZERO;
    	if(task.getTaskList() == null || task.getTaskList().isEmpty()){
    		return task.getProgress();
    	}
    	else{
    		for (Task taskIt : task.getTaskList()) {
				progress = progress.add(this.computeProgress(taskIt));
			}
    		task.setProgress(progress.divide(new BigDecimal(task.getTaskList().size()), 2, RoundingMode.HALF_UP));
    		ds.save(task);
    		return task.getProgress();
    	}
    }
    
    public void taskDone (Task task){
    	if(task.getTaskList() == null || task.getTaskList().isEmpty()){
    		task.setDone(true);
    		task.setProgress(new BigDecimal(100));
    	}
    	else{
    		for (Task taskIt : task.getTaskList()) {
    			this.taskDone(taskIt);
    		}
    		task.setDone(true);
    		task.setProgress(new BigDecimal(100));
    	}
    }
}
