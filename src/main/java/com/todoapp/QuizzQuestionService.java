package com.todoapp;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;

public class QuizzQuestionService {
 
    
    private final Morphia morphia;
    public final Datastore ds;
    QuizzCategoryService quizzCategoryService;
    
    public QuizzQuestionService(MongoClient mongoClient, String dbName, QuizzCategoryService quizzCategoryService) {
        morphia = new Morphia();
        morphia.map(QuizzQuestions.class);
        ds = morphia.createDatastore(mongoClient, dbName);
        this.quizzCategoryService = quizzCategoryService;
    }
 
    public void createNewQuest(String catId, String body) {
    	QuizzCategory category = quizzCategoryService.find(catId);
    	QuizzQuestions quest = new Gson().fromJson(body, QuizzQuestions.class);
    	HashMap<String,String> map = new Gson().fromJson(body, new TypeToken<HashMap<String, String>>(){}.getType());
    	if(map.containsKey("propositions")){
    		String propositions = map.get("propositions");
    		if(!Strings.isNullOrEmpty(propositions)){
    			List<String> propositionsList = new ArrayList<String>(Arrays.asList(propositions.split(",")));
    			quest.setPropositionsList(propositionsList);
    		}
    	}
    	category.getQuizzQuestionsList().add(quest);
    	quizzCategoryService.ds.save(category);
    }
    
    public QuizzQuestions findRandomQuestionWithCatId(String catId){
    	QuizzQuestions question = null;
    	QuizzCategory category = quizzCategoryService.find(catId);
    	int nbQuestions = 0;
    	if(category != null && category.getQuizzQuestionsList() != null){
    		nbQuestions = category.getQuizzQuestionsList().size();
    		if(nbQuestions != 0){
    			while(question == null){
    				Random randomGenerator = new Random();
        			int questId = randomGenerator.nextInt(nbQuestions);
        			question = category.getQuizzQuestionsList().get(questId);
        			if(question.getDone()){
        				question = null;
        			}
    			}
    		}
    	}
    	return question;
    }
    
    public boolean update(String catId, String questId, String answer){
    	QuizzCategory category = quizzCategoryService.find(catId);
    	QuizzQuestions questSaved = null;
    	for (QuizzQuestions questIt : category.getQuizzQuestionsList()) {
			if(questIt.getName().equals(questId)){
				questSaved = questIt;
				break;
			}
		}
    	if(questSaved != null){
    		questSaved.setDone(true);
    		quizzCategoryService.ds.save(category);
        	if(answer.equals(questSaved.getResponse())){
        		return true;
        	}
        	else return false;
    	}
    	return false;
    }
}