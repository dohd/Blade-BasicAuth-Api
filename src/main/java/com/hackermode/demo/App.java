package com.hackermode.demo;

import com.blade.Blade;

public class App 
{
    public static void main(String[] args) throws Exception {
        Blade blade = Blade.of();
        blade.before("/auth/logout", ctx -> CookieService.verify(ctx));
        blade.before("/auth/password-reset/:id", ctx -> CookieService.verify(ctx));
        blade.start(App.class, args);
    }
}