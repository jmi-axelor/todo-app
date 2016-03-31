package com.todoapp;
 
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import spark.Request;
import spark.Response;
import spark.Route;

public class QuizzCategoryResource {
 
    private static final String API_CONTEXT = "/api/v1";
 
    private final QuizzCategoryService quizzCategoryService;
 
    public QuizzCategoryResource(QuizzCategoryService quizzCategoryService) {
        this.quizzCategoryService = quizzCategoryService;
        setupEndpoints();
    }
 
    private void setupEndpoints() {
 
        get(API_CONTEXT + "/quizzCats", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return quizzCategoryService.findAll();
            }
        }, new JsonTransformer());
        
        get(API_CONTEXT + "/quizzCat/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return quizzCategoryService.find(request.params(":id"));
            }
        }, new JsonTransformer());
        
        get(API_CONTEXT + "/playCat/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return quizzCategoryService.findNextQuestion(request.params(":id"));
            }
        }, new JsonTransformer());
        
        get(API_CONTEXT + "/findNextQuestion/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return quizzCategoryService.findNextQuestion(request.params(":id"));
            }
        }, new JsonTransformer());
        
        post(API_CONTEXT + "/cat", "application/json", new Route() {
            public Object handle(Request request, Response response) {
            	quizzCategoryService.createNewCat(request.body());
	            response.status(201);
	            return response;
            }
        }, new JsonTransformer());
        
        put(API_CONTEXT + "/restartCat/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	quizzCategoryService.restart(request.params(":id"));
            	response.status(201);
	            return response;
            }
        }, new JsonTransformer());
        
        put(API_CONTEXT + "/deleteCat/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	quizzCategoryService.delete(request.params(":id"));
            	response.status(201);
	            return response;
            }
        }, new JsonTransformer());
    }
 
 
}