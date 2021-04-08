package com.hackermode.demo;

import com.blade.exception.BladeException;
import com.blade.ioc.annotation.Bean;
import com.blade.mvc.WebContext;
import com.blade.mvc.handler.DefaultExceptionHandler;
import com.blade.mvc.ui.RestResponse;

@Bean
public class GlobalExceptionHandler extends DefaultExceptionHandler {
    
    @Override
    public void handle(Exception e) {
        if (e instanceof BladeException) {
            BladeException bladeException = (BladeException) e;
            String msg = bladeException.getMessage();
            Integer code = bladeException.getStatus();
            WebContext.response().json(RestResponse.fail(code, msg));
        } else {
            super.handle(e);
        }
    }
}
