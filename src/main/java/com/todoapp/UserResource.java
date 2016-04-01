package com.todoapp;
 
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import spark.Request;
import spark.Response;
import spark.Route;

public class UserResource {
 
    private static final String API_CONTEXT = "/api/v1";
 
    private final UserService userService;
 
    public UserResource(UserService userService) {
        this.userService = userService;
        setupEndpoints();
    }
 
    private void setupEndpoints() {
        post(API_CONTEXT + "/users", "application/json", new Route() {
            public Object handle(Request request, Response response) {
            	userService.createNewUser(request.body());
	            response.status(201);
	            return response;
            }
        }, new JsonTransformer());
 
        get(API_CONTEXT + "/users/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return userService.find(request.params(":id"));
            }
        }, new JsonTransformer());
 
        get(API_CONTEXT + "/users", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return userService.findAll();
            }
        }, new JsonTransformer());
 
        put(API_CONTEXT + "/users/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return userService.update(request.params(":id"), request.body());
            }
        }, new JsonTransformer());
        
        put(API_CONTEXT + "/delete", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	userService.delete(request.body());
            	response.status(201);
	            return response;
            }
        }, new JsonTransformer());
    }
 
 
}