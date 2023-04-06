package com.ue.urlshortener.application.dto;

import com.ue.urlshortener.domain.UrlPointer;

public record UrlPointerDto(String target, String targetIdentifier) {
    public UrlPointerDto(UrlPointer pointer) { // mapstruct can be utilized with entity/beans with more fields
        this(pointer.getTarget(), pointer.getTargetIdentifier());
    }
}
