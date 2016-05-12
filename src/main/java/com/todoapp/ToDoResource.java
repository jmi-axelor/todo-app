package com.todoapp;
 
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;

import spark.Request;
import spark.Response;
import spark.Route;

public class ToDoResource {
 
    private static final String API_CONTEXT = "/api/v1";
 
    private final ToDoService todoService;
 
    public ToDoResource(ToDoService todoService) {
        this.todoService = todoService;
        setupEndpoints();
    }
 
    private void setupEndpoints() {
        post(API_CONTEXT + "/todos", "application/json", new Route() {
            public Object handle(Request request, Response response) {
	            todoService.createNewTodo(request.body());
	            response.status(201);
	            return response;
            }
        }, new JsonTransformer());
 
        get(API_CONTEXT + "/todos/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return todoService.find(Integer.parseInt(request.params(":id")));
            }
        }, new JsonTransformer());
 
        get(API_CONTEXT + "/todos", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
		        if(request.session().attribute("username") == null){
		     		return false;
		     	}
            	return todoService.findAll();
            }
        }, new JsonTransformer());
 
        put(API_CONTEXT + "/todos/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return todoService.update(request.body());
            }
        }, new JsonTransformer());
        
        put(API_CONTEXT + "/delete/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	todoService.delete(Integer.parseInt(request.params(":id")));
            	response.status(201);
	            return response;
            }
        }, new JsonTransformer());
        
        put(API_CONTEXT + "/taskDone/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	todoService.taskDone(Integer.parseInt(request.params(":id")), request.body());
            	response.status(201);
	            return response;
            }
        }, new JsonTransformer());
        
        post(API_CONTEXT + "/newTask/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) {
            	todoService.createNewTask(request.params(":id"), request.body());
	            response.status(201);
	            return response;
            }
        }, new JsonTransformer());
        
        post(API_CONTEXT + "/newTask/:todoId/:taskId", "application/json", new Route() {
            public Object handle(Request request, Response response) {
        		todoService.createNewTask(Integer.parseInt(request.params(":todoId")), Integer.parseInt(request.params(":taskId")), request.body());
        		response.status(201);
	            
	            return response;
            }
        }, new JsonTransformer());
        
        delete(API_CONTEXT + "/deleteTask/:todoId/:taskId", "application/json", new Route() {
            public Object handle(Request request, Response response) {
        		todoService.deleteTask(Integer.parseInt(request.params(":todoId")), Integer.parseInt(request.params(":taskId")));
        		response.status(201);
	            
	            return response;
            }
        }, new JsonTransformer());
    }
 
 
}