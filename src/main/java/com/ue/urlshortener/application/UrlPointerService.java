package com.ue.urlshortener.application;

import com.ue.urlshortener.application.dto.UrlPointerDto;
import com.ue.urlshortener.application.hasher.UrlHasher;
import com.ue.urlshortener.domain.UrlPointer;
import com.ue.urlshortener.domain.UrlPointerRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UrlPointerService {
    private UrlHasher urlHasher;
    private UrlPointerRepository repository;

    public UrlPointerDto shortenUrl(String target) {
        var existingPointerOpt = repository.findByTarget(target);
        if (existingPointerOpt.isPresent()) {
            return new UrlPointerDto(existingPointerOpt.get());
        }

        var targetIdentifier = urlHasher.hash(target);
        var urlPointer = new UrlPointer(target, targetIdentifier);
        repository.save(urlPointer);
        return new UrlPointerDto(urlPointer);
    }

    public Optional<UrlPointerDto> getTarget(String targetIdentifier) {
        var urlPointer = repository.findByTargetIdentifier(targetIdentifier);
        return urlPointer.map(UrlPointerDto::new);
    }

}
