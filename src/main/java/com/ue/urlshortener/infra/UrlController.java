package com.ue.urlshortener.infra;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.unprocessableEntity;

import com.ue.urlshortener.application.UrlPointerService;
import com.ue.urlshortener.application.dto.UrlPointerDto;
import com.ue.urlshortener.infra.request.UrlShortenRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/point")
@AllArgsConstructor
public class UrlController {
    private UrlPointerService urlPointerService;

    @PostMapping(value = "/point")
    public ResponseEntity<UrlPointerDto> shortenUrl(@RequestHeader("from") String owner, // https would be needed to secure user information
                                                    @RequestBody UrlShortenRequest req) { // validate request
        var resp = urlPointerService.shortenUrl(owner, req.targetUrl());
        return ok(resp);
    }

    @GetMapping
    public ResponseEntity getTargetUrl() { // Clarify target url in readme as it'd be a part of ubiquitous language
        //TODO : implement
        return unprocessableEntity().build();
    }

    @PutMapping
    public ResponseEntity updateUrlTarget() {
        //TODO : implement
        // can't update record that doesn't belong to client
        return unprocessableEntity().build();
    }
}
