package com.hackermode.demo;

import com.blade.Blade;
import com.blade.ioc.annotation.Bean;
import com.blade.loader.BladeLoader;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@Bean
public class MongoConfig implements BladeLoader {

    private static MongoDatabase database;

    @Override
    public void load (Blade blade) {
        MongoClient client = MongoClients.create();
        database =  client.getDatabase("foodapp");

        String msg = "\nConnected to %s database successfully!\n";
        System.out.println(String.format(msg, database.getName()));
    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}