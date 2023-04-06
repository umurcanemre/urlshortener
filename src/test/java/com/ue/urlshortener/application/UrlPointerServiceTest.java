package com.ue.urlshortener.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.ue.urlshortener.application.hasher.UrlHasher;
import com.ue.urlshortener.domain.UrlPointer;
import com.ue.urlshortener.domain.UrlPointerRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UrlPointerServiceTest {

    UrlPointerService service;

    private UrlHasher urlHasher;

    private UrlPointerRepository repository;

    private static final String TARGET = "https://www.youtube.com/watch?v=iiMFRMoxxEI";
    private static final String TARGET_ID = "SH0rT";

    @BeforeEach
    void setup() {
        urlHasher = mock(UrlHasher.class);
        repository = mock(UrlPointerRepository.class);
        service = new UrlPointerService(urlHasher, repository);
    }

    @Test
    void shouldShortenUrlWhenDoesntAlreadyExist() {
        when(repository.findByTarget(TARGET)).thenReturn(Optional.empty());
        when(urlHasher.hash(TARGET)).thenReturn(TARGET_ID);

        var actual = service.shortenUrl(TARGET);

        assertThat(actual.target()).isEqualTo(TARGET);
        assertThat(actual.targetIdentifier()).isEqualTo(TARGET_ID);

        var urlPointerCaptor = ArgumentCaptor.forClass(UrlPointer.class);
        verify(urlHasher).hash(TARGET);
        verify(repository).findByTarget(TARGET);
        verify(repository).save(urlPointerCaptor.capture());
        verifyNoMoreInteractions(repository, urlHasher);
    }

    @Test
    void shouldReturnExistingShortUrlWhenAlreadyExist() {
        when(repository.findByTarget(TARGET))
                .thenReturn(Optional.of(new UrlPointer(TARGET, TARGET_ID)));

        var actual = service.shortenUrl(TARGET);

        assertThat(actual.target()).isEqualTo(TARGET);
        assertThat(actual.targetIdentifier()).isEqualTo(TARGET_ID);
        verify(repository).findByTarget(TARGET);
        verifyNoMoreInteractions(repository, urlHasher);
    }
}