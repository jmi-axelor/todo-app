package com.todoapp;
 
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.gson.Gson;
import com.mongodb.MongoClient;

public class ToDoService {
 
    
    private final Datastore ds;
    public SequenceService sequenceService; 
    public TaskService taskService;
 
    public ToDoService(MongoClient mongoClient, String dbName, Morphia morphia, SequenceService sequenceService, TaskService taskService) {
        morphia.map(ToDo.class);
        ds = morphia.createDatastore(mongoClient, dbName);
        this.sequenceService = sequenceService;
        this.taskService = taskService;
    }
 
    public List<ToDo> findAll() {
        List<ToDo> todos = ds.find(ToDo.class).asList();
        return todos;
    }
 
    public void createNewTodo(String body) {
        ToDo todo = new Gson().fromJson(body, ToDo.class);
        todo.setProgress(BigDecimal.ZERO);
        todo.setId(sequenceService.getNextValue(ToDo.class.getName()));
        ds.save(todo);
    }
 
    public ToDo find(int id) {
        return ds.get(ToDo.class, id);
    }
 
    public ToDo update(String body) {
        ToDo todo = new Gson().fromJson(body, ToDo.class);
        if(todo.getDone()){
        	todo.setProgress(new BigDecimal(100));
            for (Task task : todo.getTaskList()) {
    			taskService.taskDone(task);
    		}
        }
        ds.save(todo);
        return this.find(todo.getId());
    }
    
    public void delete(int id) {
        ds.delete(this.find(id));
    }
    
    public void computeProgress(ToDo todo){
    	BigDecimal progress = BigDecimal.ZERO;
    	List<Task> taskList = todo.getTaskList();
    	if(todo.getDone()){
    		progress = new BigDecimal(100);
    	}
    	else if(taskList != null && !taskList.isEmpty()){
    		for (Task taskIt : todo.getTaskList()) {
				progress = progress.add(taskService.computeProgress(taskIt));
			}
    		progress = progress.divide(new BigDecimal(todo.getTaskList().size()), 2, RoundingMode.HALF_UP);
    		if(progress.compareTo(new BigDecimal(100)) == 0){
    			todo.setDone(true);
    		}
    	}
    	todo.setProgress(progress);
    	ds.save(todo);
    }
    
    public void taskDone(int todoId, String taskJson){
    	Task task = new Gson().fromJson(taskJson, Task.class);
    	ToDo todo = this.find(todoId);
    	Task persistedTask = taskService.find(todo, task.getId());
    	if(task.getDone()){
    		persistedTask.setDone(true);
    		persistedTask.setProgress(new BigDecimal(100));
    		taskService.setDoneForChilds(persistedTask);
    	}
    	else{
    		persistedTask.setDone(false);
    		persistedTask.setProgress(BigDecimal.ZERO);
    		persistedTask.setProgress(taskService.computeProgress(persistedTask));
    		taskService.setUndoneForParents(todo, persistedTask);
    	}
//		Find task, update it and compute all progressions
//		Query<ToDo> query = ds.createQuery(ToDo.class).field("_id").equal(todoId).field("taskList.id").equal(task.getId());
//		UpdateOperations<ToDo> ops = ds.createUpdateOperations(ToDo.class).set("taskList.$", task);
    	ds.save(todo);
    	this.computeProgress(todo);
    }
    
    public void createNewTask(String todoId, String body) {
    	ToDo todo = this.find(Integer.parseInt(todoId));
    	Task task = new Gson().fromJson(body, Task.class);
    	task.setProgress(BigDecimal.ZERO);
    	task.setId(sequenceService.getNextValue(Task.class.getName()));
    	task.setParentTodoId(Integer.parseInt(todoId));
    	List<Task> taskList = todo.getTaskList();
    	if(taskList == null){
    		taskList = new ArrayList<Task>();
    	}
    	taskList.add(task);
    	todo.setTaskList(taskList);
    	this.computeProgress(todo);
    	ds.save(todo);
    }
    
    public void createNewTask(int todoId, int taskId, String body) {
    	ToDo todo = this.find(todoId);
    	Task parentTask = taskService.find(todo, taskId);
    	Task task = new Gson().fromJson(body, Task.class);
    	task.setProgress(BigDecimal.ZERO);
    	task.setId(sequenceService.getNextValue(Task.class.getName()));
    	task.setParentTaskId(taskId);
    	task.setParentTodoId(todoId);
    	List<Task> taskList = parentTask.getTaskList();
    	if(taskList == null){
    		taskList = new ArrayList<Task>();
    	}
    	taskList.add(task);
    	parentTask.setTaskList(taskList);
    	this.computeProgress(todo);
    	ds.save(todo);
    }
    
    public void deleteTask(int todoId, int taskId){
    	ToDo todo = this.find(todoId);
    	todo = taskService.delete(todo, taskId);
    	this.computeProgress(todo);
    	ds.save(todo);
    }
}