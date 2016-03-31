package com.todoapp;
 
import java.math.BigDecimal;
import java.math.RoundingMode;
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
 
    
    public final Datastore ds;
    public SequenceService sequenceService;
 
    public QuizzCategoryService(MongoClient mongoClient, String dbName, Morphia morphia, SequenceService sequenceService) {
        morphia.map(QuizzCategory.class);
        ds = morphia.createDatastore(mongoClient, dbName);
        this.sequenceService = sequenceService;
    }
 
    public List<QuizzCategory> findAll() {
        List<QuizzCategory> quizzCategories = ds.find(QuizzCategory.class).asList();
        return quizzCategories;
    }
    
    public QuizzCategory find(String id) {
        return ds.get(QuizzCategory.class, Integer.valueOf(id));
    }
    
    public String findQuestion(String id) {
    	QuizzCategory cat = ds.get(QuizzCategory.class, Integer.valueOf(id));
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
    	QuizzCategory cat = ds.get(QuizzCategory.class, Integer.valueOf(id));
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
    	cat.setId(sequenceService.getNextValue(QuizzCategory.class.getName()));
        ds.save(cat);
    }
    
    public void computePercentageDone(QuizzCategory cat){
    	int nbQuestsDone = 0;
    	BigDecimal percentage = BigDecimal.ZERO;
    	if(cat.getQuizzQuestionsList() != null && !cat.getQuizzQuestionsList().isEmpty()){
    		for(QuizzQuestions quizzQuestion : cat.getQuizzQuestionsList()){
        		if(quizzQuestion.getDone()){
        			nbQuestsDone ++;
        		}
        	}
    		percentage = new BigDecimal(nbQuestsDone).multiply(new BigDecimal(100))
    							.divide(new BigDecimal(cat.getQuizzQuestionsList().size()), 2, RoundingMode.HALF_UP);
    	}
    	cat.setPercentageDone(percentage);
    }
}