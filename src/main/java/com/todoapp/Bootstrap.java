package com.todoapp;
 
import static spark.SparkBase.setIpAddress;
import static spark.SparkBase.setPort;
import static spark.SparkBase.staticFileLocation;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
 
public class Bootstrap {
 
	private static final String IP_ADDRESS = System.getenv("OPENSHIFT_DIY_IP") != null ? System.getenv("OPENSHIFT_DIY_IP") : "localhost";
    private static final int PORT = System.getenv("OPENSHIFT_DIY_PORT") != null ? Integer.parseInt(System.getenv("OPENSHIFT_DIY_PORT")) : 8080;
    private static final String dbname = System.getenv("OPENSHIFT_APP_NAME") != null ? System.getenv("OPENSHIFT_APP_NAME") : "todoapp";
 
    public static void main(String[] args) throws Exception {
        setIpAddress(IP_ADDRESS);
        setPort(PORT);
        staticFileLocation("/public");
        Sequence questionSequence = new Sequence("QuizzQuestions", 1);
        Sequence categorySequence = new Sequence("QuizzCategory", 1);
        new ToDoResource(new ToDoService(mongo(), dbname));
        new UserResource(new UserService(mongo(), dbname));
        QuizzCategoryService quizzCategoryService = new QuizzCategoryService(mongo(), dbname, categorySequence);
        new QuizzCategoryResource(quizzCategoryService);
        new QuizzQuestionResource(new QuizzQuestionService(mongo(), dbname, quizzCategoryService, questionSequence));
    }
 
	private static MongoClient mongo() throws Exception {
    	String host = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
        if (host == null) {
            MongoClient mongoClient = new MongoClient("localhost");
            return mongoClient;
        }
        int port = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));
        String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
        String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add( MongoCredential.createMongoCRCredential(username, dbname, password.toCharArray()));
        MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), credentials, mongoClientOptions);
        return mongoClient;
    }
}