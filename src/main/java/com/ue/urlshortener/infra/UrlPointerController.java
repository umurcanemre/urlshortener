package com.ue.urlshortener.infra;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import com.ue.urlshortener.application.UrlPointerService;
import com.ue.urlshortener.application.dto.UrlPointerDto;
import com.ue.urlshortener.infra.request.UrlShortenRequest;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/point")
@AllArgsConstructor
@Validated
public class UrlPointerController {
    private UrlPointerService urlPointerService;

    @PostMapping
    public ResponseEntity<UrlPointerDto> shortenUrl(@RequestBody UrlShortenRequest req) { // URL object offers url validity so at the moment no further validation is needed
        var resp = urlPointerService.shortenUrl(req.targetUrl().toString());
        return ok(resp); //Could have been "created" type response, would have to deal with already exists case.
    }

    @GetMapping(value = "/{targetIdentifier}")
    public ResponseEntity<Void> getTargetUrl(@PathVariable @Pattern(regexp = "^[A-Za-z0-9]+$") String targetIdentifier) { // Check if given constraints leak any business/personal/configuration information that can be misused
        // Clarify target url in readme as it'd be a part of ubiquitous language
        var resp = urlPointerService.getTarget(targetIdentifier);
        if (resp.isEmpty()) {
            return noContent().build(); //noContent is preferred over 404 because, in the case 4xx errors were to be studied to improve user/developer experience, 404s could pose as a false negative
        }
        var headers = new HttpHeaders();
        headers.add("Location", resp.get().target());
        return new ResponseEntity<>(headers, HttpStatus.FOUND); //returning a 307 is considered but it puts heavy emphasis on the temporary nature so found was used
    }
}
