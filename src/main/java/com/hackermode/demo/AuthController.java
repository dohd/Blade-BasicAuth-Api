package com.hackermode.demo;

import com.blade.exception.BladeException;
import com.blade.mvc.annotation.BodyParam;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PathParam;
import com.blade.mvc.annotation.PostRoute;
import com.blade.mvc.annotation.PutRoute;
import com.blade.mvc.http.Response;

@Path("/auth")
public class AuthController {
    
    @PostRoute("/register")
    public void register(@BodyParam User body, Response res) { 
        try {
            String token = AuthService.register(body);
            res.json(token);
        } catch (Exception e) {
            if (e instanceof BladeException) throw e;
            ExceptionHandler.handle(e, res);
        }
    }

    @PostRoute("/login")
    public void login(@BodyParam User body, Response res) {
        try {
            String token = AuthService.login(body);
            res.json(token);
        } catch (Exception e) {
            if (e instanceof BladeException) throw e;
            ExceptionHandler.handle(e, res);
        }
    }

    @PostRoute("/logout")
    public void logout(@BodyParam User body, Response res) {
        try {
            String count = AuthService.logout(body);
            res.json(count);
        } catch (Exception e) {
            if (e instanceof BladeException) throw e;
            ExceptionHandler.handle(e, res);
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
            String user = AuthService.passwordReset(id, body);
            res.json(user);
        } catch (Exception e) {
            if (e instanceof BladeException) throw e;
            ExceptionHandler.handle(e, res);
        }
    }
}
