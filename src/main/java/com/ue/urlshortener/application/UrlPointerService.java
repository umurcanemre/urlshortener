package com.ue.urlshortener.application;

import com.ue.urlshortener.application.dto.UrlPointerDto;
import com.ue.urlshortener.application.hasher.UrlHasher;
import com.ue.urlshortener.domain.UrlPointer;
import com.ue.urlshortener.domain.UrlPointerRepository;
import java.net.URL;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UrlPointerService {
    private UrlHasher urlHasher;
    private UrlPointerRepository repository;

    public UrlPointerDto shortenUrl(String owner, URL target) {
        var existingPointerOpt = repository.findByTarget(target.toString());
        if (existingPointerOpt.isPresent()) {
            return new UrlPointerDto(existingPointerOpt.get());
        }

        var targetIdentifier = urlHasher.hash(target);
        var urlPointer = new UrlPointer(target.toString(), targetIdentifier, owner);
        repository.save(urlPointer);
        return new UrlPointerDto(urlPointer);
    }

}
