package com.todoapp;
 
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import spark.Request;
import spark.Response;
import spark.Route;

public class QuizzQuestionResource {
 
    private static final String API_CONTEXT = "/api/v1";
 
    private final QuizzQuestionService quizzQuestionService;
 
    public QuizzQuestionResource(QuizzQuestionService quizzQuestionService) {
        this.quizzQuestionService = quizzQuestionService;
        setupEndpoints();
    }
 
    private void setupEndpoints() {
    	
    	get(API_CONTEXT + "/quest/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return quizzQuestionService.findRandomQuestionWithCatId(request.params(":id"));
            }
        }, new JsonTransformer());
    	
        post(API_CONTEXT + "/newQuest/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) {
            	quizzQuestionService.createNewQuest(request.params(":id"), request.body());
	            response.status(201);
	            return response;
            }
        }, new JsonTransformer());
        
        put(API_CONTEXT + "/updateQuest/:id/:questId", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return quizzQuestionService.update(request.params(":id"), request.params(":questId"),request.body());
            }
        }, new JsonTransformer());
        
        put(API_CONTEXT + "/deleteQuest/:id", "application/json", new Route() {
            public Object handle(Request request, Response response) { 
            	return quizzQuestionService.delete(request.params(":id"), request.body());
            }
        }, new JsonTransformer());
    }
 
 
}