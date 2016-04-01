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
public class ToDoService {
 
    
    private final Datastore ds;
    public SequenceService sequenceService; 
 
    public ToDoService(MongoClient mongoClient, String dbName, Morphia morphia, SequenceService sequenceService) {
        morphia = new Morphia();
        morphia.map(ToDo.class);
        ds = morphia.createDatastore(mongoClient, dbName);
        this.sequenceService = sequenceService;
    }
 
    public List<ToDo> findAll() {
        List<ToDo> todos = ds.find(ToDo.class).asList();
        return todos;
    }
 
    public void createNewTodo(String body) {
        ToDo todo = new Gson().fromJson(body, ToDo.class);
        todo.setId(sequenceService.getNextValue(ToDo.class.getName()));
        ds.save(todo);
    }
 
    public ToDo find(int id) {
        return ds.get(ToDo.class, id);
    }
 
    public ToDo update(String body) {
        ToDo todo = new Gson().fromJson(body, ToDo.class);
        ds.save(todo);
        return this.find(todo.getId());
    }
    
    public void delete(int id) {
        ds.delete(this.find(id));
    }
}