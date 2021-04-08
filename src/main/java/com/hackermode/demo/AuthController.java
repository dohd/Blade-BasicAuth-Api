package com.hackermode.demo;

import com.blade.exception.BadRequestException;
import com.blade.exception.BladeException;
import com.blade.mvc.http.Response;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class AuthController {
    
    public static void register(User body, Response res) {
        try {
            MongoDatabase db = MongoConfig.getDatabase();
            MongoCollection<Document> collection = db.getCollection("users");

            Bson name_filter = Filters.eq("name", body.getName());
            Document prev_user = collection.find(name_filter).first();
            Boolean isExist = prev_user != null && prev_user.getString("name") != null;
            if (isExist) throw new BadRequestException("User already exists");
    
            Document user = new Document("userId", new ObjectId().toHexString());
            user.append("name", body.getName());
            user.append("password", body.getPassword());
    
            InsertOneResult user_result =  collection.insertOne(user);
            Bson id_filter = Filters.eq("_id", user_result.getInsertedId());
            Document saved_user = collection.find(id_filter).first();

            String userId = saved_user.getString("userId");
            Document session = new Document("userId", userId);
            session.append("sessionId", new ObjectId().toHexString());

            InsertOneResult session_result =  db.getCollection("sessions").insertOne(session);
            Bson session_filter = Filters.eq("_id", session_result.getInsertedId());
            Document saved_session = db.getCollection("sessions").find(session_filter).first();
            String sessionId = saved_session.getString("sessionId");

            Document token = new Document("token", sessionId);
            res.json(token.toJson());
        } catch (Exception e) {
            if (e instanceof BladeException) throw e;
            ExceptionHandler.handle(e, res);
        }
    }

    public static void login(User body, Response res) {
        try {
            MongoDatabase db = MongoConfig.getDatabase();
            MongoCollection<Document> collection = db.getCollection("users");

            Bson name_filter = Filters.eq("name", body.getName());
            Document user = collection.find(name_filter).first();
            Boolean isExist = user != null;
            
            if (!isExist) throw new BadRequestException("Invalid username or password");
            if (isExist) {
                Boolean isValidPass = user.getString("password").equals(body.getPassword());
                if (!isValidPass) throw new BadRequestException("Invalid username or password");
            }

            String userId = user.getString("userId");
            Document session = new Document("userId", userId);
            session.append("sessionId", new ObjectId().toHexString());

            InsertOneResult result =  db.getCollection("sessions").insertOne(session);
            Bson session_filter = Filters.eq("_id", result.getInsertedId());
            Document user_session = db.getCollection("sessions").find(session_filter).first();
            String sessionId = user_session.getString("sessionId");

            Document token = new Document("token", sessionId);
            res.json(token.toJson());
        } catch (Exception e) {
            if (e instanceof BladeException) throw e;
            ExceptionHandler.handle(e, res);
        }
    }

    public static void logout(User body, Response res) {
        try {
            MongoDatabase db = MongoConfig.getDatabase();
            MongoCollection<Document> collection = db.getCollection("sessions");
            
            Bson filter = Filters.eq("userId", body.getUserId());
            DeleteResult result = collection.deleteOne(filter);
            Document doc = new Document("count", result.getDeletedCount());
            res.json(doc.toJson());
        } catch (Exception e) {
            if (e instanceof BladeException) throw e;
            ExceptionHandler.handle(e, res);
        }
    }

    public static void passwordReset(String id, User body, Response res) 
    {
        try {
            Boolean hasPass =  body.getPassword() != null;
            if (!hasPass) throw new BadRequestException("Password is required!");

            MongoDatabase db = MongoConfig.getDatabase();
            MongoCollection<Document> collection = db.getCollection("users");

            Bson filter = Filters.eq("userId", id);
            Bson update = Updates.set("password", body.getPassword());
            Document user = collection.findOneAndUpdate(filter, update);
            
            if (user == null) throw new BadRequestException("Invalid userId!");
            res.json(user.toJson());
        } catch (Exception e) {
            if (e instanceof BladeException) throw e;
            ExceptionHandler.handle(e, res);
        }
    }
}
