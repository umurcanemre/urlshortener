package com.ue.urlshortener.infra;

import jakarta.validation.ConstraintViolationException;
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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleConstraintViolation() {
        // no explanation given to not leak decisions
    }
}
