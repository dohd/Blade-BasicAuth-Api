package com.hackermode.demo;

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
        AuthService.register(body, res); 
    }

    @PostRoute("/login")
    public void login(@BodyParam User body, Response res) {
        AuthService.login(body, res);
    }

    @PostRoute("/logout")
    public void logout(@BodyParam User body, Response res) {
        AuthService.logout(body, res);
    }

    @PutRoute("/password-reset/:id")
    public void passwordReset(
        @PathParam String id, 
        @BodyParam User body, 
        Response res
    ) { AuthService.passwordReset(id, body, res); }
}
