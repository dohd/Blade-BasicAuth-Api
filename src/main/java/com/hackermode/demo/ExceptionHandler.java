package com.hackermode.demo;

import com.blade.mvc.http.Response;

public class ExceptionHandler {

    public static void handle(Exception e, Response res) {
        System.out.println("\n" + e);
        res.status(500).json("Internal Server Error");
    }
}
