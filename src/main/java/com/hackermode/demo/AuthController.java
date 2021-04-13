package com.hackermode.demo;

import com.blade.exception.BladeException;
import com.blade.mvc.annotation.BodyParam;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PathParam;
import com.blade.mvc.annotation.PostRoute;
import com.blade.mvc.annotation.PutRoute;
import com.blade.mvc.http.Response;

import org.bson.Document;

@Path("/auth")
public class AuthController {
    
    @PostRoute("/register")
    public void register(@BodyParam User body, Response res) { 
        try {
            Document result = AuthService.register(body);
            String sessionId = result.getString("sessionId");

            res.cookie("sessionId", sessionId, 60*60*24, true);
            result.remove("sessionId");

            res.json(result.toJson());
        } catch (Exception e) {
            if (e instanceof BladeException) throw e;
            res.status(500).json("Internal Server Error");
        }
    }

    @PostRoute("/login")
    public void login(@BodyParam User body, Response res) {
        try {
            Document result = AuthService.login(body);
            String sessionId = result.getString("sessionId");

            res.cookie("sessionId", sessionId, 60*60*24, true);
            result.remove("sessionId");

            res.json(result.toJson());
        } catch (Exception e) {
            if (e instanceof BladeException) throw e;
            res.status(500).json("Internal Server Error");
        }
    }

    @PostRoute("/logout")
    public void logout(@BodyParam User body, Response res) {
        try {
            String result = AuthService.logout(body);
            res.json(result);
        } catch (Exception e) {
            if (e instanceof BladeException) throw e;
            res.status(500).json("Internal Server Error");
        }
    }

    @PutRoute("/password-reset/:id")
    public void passwordReset(
        @PathParam String id, 
        @BodyParam User body, 
        Response res
    ) 
    { 
        try {
            String result = AuthService.passwordReset(id, body);
            res.json(result);
        } catch (Exception e) {
            if (e instanceof BladeException) throw e;
            res.status(500).json("Internal Server Error");
        }
    }
}
