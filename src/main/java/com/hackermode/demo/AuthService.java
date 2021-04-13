package com.hackermode.demo;

import com.blade.exception.BadRequestException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class AuthService {
    
    public static Document register(User body) {
        MongoDatabase db = MongoConfig.getDatabase();
        MongoCollection<Document> Users = db.getCollection("users");
        MongoCollection<Document> Sessions = db.getCollection("sessions");

        Bson name_filter = Filters.eq("name", body.getName());
        Document prev_user = Users.find(name_filter).first();
        Boolean userExists = prev_user != null;
        if (userExists) throw new BadRequestException("User already exists");

        Document user = new Document("userId", new ObjectId().toHexString());
        user.append("name", body.getName());
        user.append("password", body.getPassword());

        InsertOneResult user_result =  Users.insertOne(user);
        Bson id_filter = Filters.eq("_id", user_result.getInsertedId());
        Document saved_user = Users.find(id_filter).first();

        String userId = saved_user.getString("userId");
        Document session = new Document("userId", userId);
        session.append("sessionId", new ObjectId().toHexString());

        InsertOneResult session_result =  Sessions.insertOne(session);
        Bson session_filter = Filters.eq("_id", session_result.getInsertedId());
        Document saved_session = Sessions.find(session_filter).first();
        String sessionId = saved_session.getString("sessionId");

        Document response = new Document("userId", userId);
        response.append("sessionId", sessionId);
        return response;
    }

    public static Document login(User body) {
        MongoDatabase db = MongoConfig.getDatabase();
        MongoCollection<Document> Users = db.getCollection("users");
        MongoCollection<Document> Sessions = db.getCollection("sessions");

        Bson name_filter = Filters.eq("name", body.getName());
        Document user = Users.find(name_filter).first();
        Boolean userExists = user != null;
        
        if (!userExists) throw new BadRequestException("Invalid name or password");
        Boolean isValidPass = user.getString("password").equals(body.getPassword());
        if (!isValidPass) throw new BadRequestException("Invalid name or password");

        String userId = user.getString("userId");
        Document session = new Document("userId", userId);
        session.append("sessionId", new ObjectId().toHexString());

        InsertOneResult session_result =  Sessions.insertOne(session);
        Bson session_filter = Filters.eq("_id", session_result.getInsertedId());
        Document user_session = Sessions.find(session_filter).first();
        String sessionId = user_session.getString("sessionId");

        Document response = new Document("userId", userId);
        response.append("sessionId", sessionId);
        return response;
    }

    public static String logout(User body) {
        MongoDatabase db = MongoConfig.getDatabase();
        MongoCollection<Document> Sessions = db.getCollection("sessions");
        
        Bson filter = Filters.eq("userId", body.getUserId());
        DeleteResult result = Sessions.deleteOne(filter);
        Document response = new Document("count", result.getDeletedCount());
        return response.toJson();
    }

    public static String passwordReset(String id, User body) {
        MongoDatabase db = MongoConfig.getDatabase();
        MongoCollection<Document> Users = db.getCollection("users");

        Boolean hasPass =  body.getPassword() != null;
        if (!hasPass) throw new BadRequestException("Password is required!");

        Bson filter = Filters.eq("userId", id);
        Bson update = Updates.set("password", body.getPassword());
        Document user = Users.findOneAndUpdate(filter, update);        
        if (user == null) throw new BadRequestException("Invalid userId!");
        
        Document response = new Document("update", 1);
        return response.toJson();
    }
}
