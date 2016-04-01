package com.todoapp;
 
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.gson.Gson;
import com.mongodb.MongoClient;

@SuppressWarnings("unused")
public class UserService {
 
    
    private final Datastore ds;
 
    public UserService(MongoClient mongoClient, String dbName, Morphia morphia) {
        morphia.map(User.class);
        ds = morphia.createDatastore(mongoClient, dbName);
    }
 
    public List<User> findAll() {
        List<User> users = ds.find(User.class).asList();
        return users;
    }
 
    public void createNewUser(String body) {
        User user = new Gson().fromJson(body, User.class);
        user.setPassword(CryptWithMD5.cryptWithMD5(user.getPassword()));
        ds.save(user);
    }
 
    public User find(String id) {
        return ds.get(User.class, id);
    }
 
    public User update(String id, String body) {
        User user = new Gson().fromJson(body, User.class);
        User savedUser = ds.get(User.class, id);
        //TODO
        return this.find(id);
    }
    
    public void delete(String body){
    	User user = new Gson().fromJson(body, User.class);
    	ds.delete(user);
    }
}