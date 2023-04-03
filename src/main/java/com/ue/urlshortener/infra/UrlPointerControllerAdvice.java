package com.ue.urlshortener.infra;

import java.net.MalformedURLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class UrlPointerControllerAdvice {
    @ExceptionHandler(MalformedURLException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleMalformedUrl() {
        return "Given URL is malformed";
    }
}
