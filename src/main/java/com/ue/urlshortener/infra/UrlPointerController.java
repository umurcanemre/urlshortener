package com.ue.urlshortener.infra;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.unprocessableEntity;

import com.ue.urlshortener.application.UrlPointerService;
import com.ue.urlshortener.application.dto.UrlPointerDto;
import com.ue.urlshortener.infra.request.UrlShortenRequest;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
@AllArgsConstructor
public class UrlPointerController {
    private UrlPointerService urlPointerService;

    public static final String ANON = "ANON";

    @PostMapping
    public ResponseEntity<UrlPointerDto> shortenUrl(@RequestHeader("from") Optional<String> owner, // https would be needed to secure user information
                                                    @RequestBody UrlShortenRequest req) { // validate request
        var resp = urlPointerService.shortenUrl(owner.orElse(ANON), req.targetUrl());
        return ok(resp); //Could have been "created" type response, would have to deal with already exists case.
    }

    @GetMapping(value = "/{targetIdentifier}")
    public ResponseEntity<Void> getTargetUrl(@PathVariable String targetIdentifier) { // Clarify target url in readme as it'd be a part of ubiquitous language
        var resp = urlPointerService.getTarget(targetIdentifier);
        if (resp.isEmpty()) {
            return noContent().build(); //noContent is preferred over 404 because, in the case 4xx errors were to be studied to improve user/developer experience, 404s could pose as a false negative
        }
        var headers = new HttpHeaders();
        headers.add("Location", resp.get().target());
        return new ResponseEntity<>(headers, HttpStatus.FOUND); //returning a 307 is considered but it puts heavy emphasis on the temporary nature so found was used
    }

    @PutMapping
    public ResponseEntity updateUrlTarget() {
        //TODO : implement
        // can't update record that doesn't belong to client
        return unprocessableEntity().build();
    }
}
