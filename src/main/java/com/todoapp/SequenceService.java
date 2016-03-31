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
public class SequenceService {
 
    
    private final Datastore ds;
 
    public SequenceService(MongoClient mongoClient, String dbName, Morphia morphia) {
        morphia = new Morphia();
        morphia.map(Sequence.class);
        ds = morphia.createDatastore(mongoClient, dbName);
    }
    
    public int getNextValue(Sequence sequence){
    	int value = sequence.getNextValue();
    	ds.save(sequence);
    	return value;
    }
}