package com.todoapp;
 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@SuppressWarnings("unused")
public class QuizzCategoryService {
 
    
    private final Morphia morphia;
    public final Datastore ds;
 
    public QuizzCategoryService(MongoClient mongoClient, String dbName) {
        morphia = new Morphia();
        morphia.map(QuizzCategory.class);
        ds = morphia.createDatastore(mongoClient, dbName);
    }
 
    public List<QuizzCategory> findAll() {
        List<QuizzCategory> quizzCategories = ds.find(QuizzCategory.class).asList();
        return quizzCategories;
    }
    
    public QuizzCategory find(String id) {
        return ds.get(QuizzCategory.class, id);
    }
    
    public String findQuestion(String id) {
    	QuizzCategory cat = ds.get(QuizzCategory.class, id);
    	if(cat.getQuizzQuestionsList() == null || cat.getQuizzQuestionsList().isEmpty()){
    		return null;
    	}
    	else{
    		for (QuizzQuestions question : cat.getQuizzQuestionsList()) {
				question.setDone(false);
			}
    		ds.save(cat);
    		return cat.getName();
    	}
    }
    
    public String findNextQuestion(String id) {
    	QuizzCategory cat = ds.get(QuizzCategory.class, id);
    	if(cat.getQuizzQuestionsList() == null || cat.getQuizzQuestionsList().isEmpty()){
    		return null;
    	}
    	else{
    		for (QuizzQuestions question : cat.getQuizzQuestionsList()) {
				if(!question.getDone()){
					return cat.getName();
				}
			}
    		return null;
    	}
    }
    
    public void createNewCat(String body) {
    	QuizzCategory cat = new Gson().fromJson(body, QuizzCategory.class);
        ds.save(cat);
    }
}