package com.todoapp;

import static spark.Spark.get;

import spark.Request;
import spark.Response;
import spark.Route;

public class TaskResource {
 
    private static final String API_CONTEXT = "/api/v1";
 
    private final TaskService taskService;
 
    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
        setupEndpoints();
    }
 
    private void setupEndpoints() {
    	get(API_CONTEXT + "/task/:todoId/:taskId", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return taskService.find(Integer.parseInt(request.params(":todoId")), Integer.parseInt(request.params(":taskId")));
            }
        }, new JsonTransformer());
    }
 
 
}