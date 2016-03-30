package com.todoapp;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.gson.Gson;
import com.mongodb.MongoClient;

/**
 * Created by shekhargulati on 09/06/14.
 */
public class TodoService {

	private final Morphia morphia;
    private final Datastore ds;
 
    public TodoService(MongoClient mongoClient, String dbName) {
        morphia = new Morphia();
        morphia.map(Todo.class);
        ds = morphia.createDatastore(mongoClient, dbName);
    }

    public List<Todo> findAll() {
        List<Todo> todos = ds.find(Todo.class).asList();
        return todos;
    }

    public void createNewTodo(String body) {
    	Todo todo = new Gson().fromJson(body, Todo.class);
        ds.save(todo);
    }
 
    public Todo find(String id) {
        return ds.get(Todo.class, id);
    }
 
    public Todo update(String body) {
    	Todo todo = new Gson().fromJson(body, Todo.class);
        ds.save(todo);
        return this.find(todo.getId().toString());
    }
}
