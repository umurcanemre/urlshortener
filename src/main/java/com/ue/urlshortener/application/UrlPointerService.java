package com.ue.urlshortener.application;

import com.ue.urlshortener.application.dto.UrlPointerDto;
import com.ue.urlshortener.application.hasher.UrlHasher;
import com.ue.urlshortener.domain.UrlPointer;
import com.ue.urlshortener.domain.UrlPointerRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UrlPointerService {
    private UrlHasher urlHasher;
    private UrlPointerRepository repository;

    // responsibilities of this method can expand as requirements increase(e.g. black-listing certain pages or certain users, limiting creation per user or to validate full shortened url is indeed shorter than target url etc )
    // This imperative approach would not comply with open-close principle. To address this issue post-MVP internal or external events/messages can be used to put all these requirements into a choreographed "pipeline"
    public UrlPointerDto shortenUrl(String target) {
        var existingPointerOpt = repository.findByTarget(target);
        if (existingPointerOpt.isPresent()) {
            //add metric on (returning existing pointer/newly created) to monitor re-creation attempts
            //a high ratio of re-creation attempts could mean user experience issues(unclear successful shortening) or api documentation issues
            log.info("Target {} already exists, returning found pointer", target);
            return new UrlPointerDto(existingPointerOpt.get());
        }
        var targetIdentifier = urlHasher.hash(target);
        var urlPointer = new UrlPointer(target, targetIdentifier);
        repository.save(urlPointer);
        return new UrlPointerDto(urlPointer);
    }


    // Although considering caching post-mvp, ideally instead of on application service layer, it'd be a database cache so hasher service can also utilize cached queries.
    // Consider this caching as more of a proof of concept
    @Cacheable(value = "targetIdentifiers")
    public Optional<UrlPointerDto> getTarget(String targetIdentifier) {
        var urlPointer = repository.findByTargetIdentifier(targetIdentifier);
        return urlPointer.map(UrlPointerDto::new);
    }
}
