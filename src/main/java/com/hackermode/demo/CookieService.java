package com.hackermode.demo;

import com.blade.exception.BadRequestException;
import com.blade.mvc.RouteContext;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;

public class CookieService {

    public static void verify(RouteContext ctx) {
        MongoDatabase db = MongoConfig.getDatabase();
        MongoCollection<Document> Sessions = db.getCollection("sessions");

        String sessionId = ctx.cookie("sessionId");
        Bson filter = Filters.eq("sessionId", sessionId);
        Document session = Sessions.find(filter).first();
        if (session == null) throw new BadRequestException("Unauthorized!");
    }
}