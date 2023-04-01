package com.ue.urlshortener.domain;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlPointerRepository extends CrudRepository<UrlPointer, Long> {

    Optional<UrlPointer> findByTargetIdentifier(String targetIdentifier);

    Optional<UrlPointer> findByTarget(String target);
}
