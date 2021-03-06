package com.todoapp;
 
import static spark.SparkBase.setIpAddress;
import static spark.SparkBase.setPort;
import static spark.SparkBase.staticFileLocation;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class Bootstrap {
 
	private static final String IP_ADDRESS = System.getenv("OPENSHIFT_DIY_IP") != null ? System.getenv("OPENSHIFT_DIY_IP") : "localhost";
    private static final int PORT = System.getenv("OPENSHIFT_DIY_PORT") != null ? Integer.parseInt(System.getenv("OPENSHIFT_DIY_PORT")) : 8080;
    private static final String dbname = System.getenv("OPENSHIFT_APP_NAME") != null ? System.getenv("OPENSHIFT_APP_NAME") : "todoapp";
    private static final Morphia morphia = new Morphia();
 
    public static void main(String[] args) throws Exception {
        setIpAddress(IP_ADDRESS);
        setPort(PORT);
        staticFileLocation("/public");
        morphia.getMapper().getConverters().addConverter(BigDecimalConverter.class);
        SequenceService sequenceService = new SequenceService(mongo(), dbname, morphia);
        TaskService taskService = new TaskService(mongo(), dbname, morphia, sequenceService);
        new TaskResource(taskService);
        new ToDoResource(new ToDoService(mongo(), dbname, morphia, sequenceService, taskService));
        new UserResource(new UserService(mongo(), dbname, morphia));
        QuizzCategoryService quizzCategoryService = new QuizzCategoryService(mongo(), dbname, morphia, sequenceService);
        new QuizzCategoryResource(quizzCategoryService);
        new QuizzQuestionResource(new QuizzQuestionService(mongo(), dbname, morphia, quizzCategoryService, sequenceService));
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