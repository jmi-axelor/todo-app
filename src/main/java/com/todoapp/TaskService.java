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
    
    public ToDo delete(ToDo todo, int taskId){
    	if(todo.getTaskList() == null){
    		return null;
    	}
    	for (Task task : todo.getTaskList()) {
			if(task.getId() == taskId){
				todo.getTaskList().remove(task);
				return todo;
			}
		}
    	for (Task task : todo.getTaskList()) {
    		if(this.deleteTaskInTask(task, taskId)){
				return todo;
    		}
    	}
    	return null;
    }
    
    public boolean deleteTaskInTask(Task task, int taskId){
    	if(task.getTaskList() == null){
    		return false;
    	}
    	for (Task taskIt : task.getTaskList()) {
			if(taskIt.getId() == taskId){
				task.getTaskList().remove(taskIt);
				return true;
			}
		}
    	for (Task taskIt : task.getTaskList()) {
    		if(this.deleteTaskInTask(taskIt, taskId)){
				return true;
    		}
		}
    	return false;
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
    
    public void setDoneForChilds(Task task){
    	if(task.getTaskList() == null){
    		task.setProgress(new BigDecimal(100));
    		task.setDone(true);
    		return;
    	}
    	for (Task taskIt : task.getTaskList()) {
    		this.setDoneForChilds(taskIt);
		}
    }
    
    public void setUndoneForParents(ToDo todo, Task task){
    	todo.setDone(false);
    	if(todo.getTaskList() == null || todo.getTaskList().contains(task)){
    		return;
    	}
    	for (Task taskIt : todo.getTaskList()) {
			if(this.setUndoneForTasksParent(taskIt, task)){
				taskIt.setDone(false);
			}
		}
    }
    
    public boolean setUndoneForTasksParent(Task taskIt, Task task){
    	if(taskIt.getTaskList() == null){
    		return false;
    	}
    	if(taskIt.getTaskList().contains(task)){
    		return true;
    	}
    	for (Task taskIt2 : taskIt.getTaskList()) {
			if(this.setUndoneForTasksParent(taskIt2, task)){
				taskIt2.setDone(false);
			}
		}
    	return false;
    }
}
