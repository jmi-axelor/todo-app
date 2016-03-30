package com.todoapp;
 
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

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
            	return todoService.find(request.params(":id"));
            }
        }, new JsonTransformer());
 
        get(API_CONTEXT + "/todos", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return todoService.findAll();
            }
        }, new JsonTransformer());
 
        put(API_CONTEXT + "/todos/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return todoService.update(request.body());
            }
        }, new JsonTransformer());
    }
 
 
}