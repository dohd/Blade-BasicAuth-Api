package com.hackermode.demo;

import com.blade.Blade;

public class App 
{
    public static void main(String[] args) throws Exception {
        Blade blade = Blade.of();
        blade.start(App.class, args);
    }
}