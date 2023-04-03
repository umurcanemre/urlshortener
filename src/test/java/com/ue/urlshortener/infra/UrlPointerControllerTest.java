package com.ue.urlshortener.infra;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ue.urlshortener.application.UrlPointerService;
import com.ue.urlshortener.application.dto.UrlPointerDto;
import com.ue.urlshortener.infra.request.UrlShortenRequest;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UrlPointerController.class)
class UrlPointerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UrlPointerService urlPointerService;

    private static final String TARGET_STR = "https://www.youtube.com/watch?v=36IV-FoFLlg";
    private static final String TARGET_PTR = "ABCDE";
    private static final URL TARGET_URL;

    static {
        try {
            TARGET_URL = new URL(TARGET_STR);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @SneakyThrows
    void shouldShortenUrlWithUser() {
        var req = new UrlShortenRequest(TARGET_URL);

        when(urlPointerService.shortenUrl(TARGET_URL))
                .thenReturn(new UrlPointerDto(TARGET_STR, TARGET_PTR));

        var expected = String.format("{\"target\":\"%s\",\"targetIdentifier\":\"%s\"}", TARGET_STR, TARGET_PTR);
        this.mockMvc
                .perform(post("/point")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

        verify(urlPointerService).shortenUrl(TARGET_URL);
        verifyNoMoreInteractions(urlPointerService);
    }

    @Test
    @SneakyThrows
    void shouldShortenUrlWithoutUser() {
        var req = new UrlShortenRequest(TARGET_URL);

        when(urlPointerService.shortenUrl(TARGET_URL))
                .thenReturn(new UrlPointerDto(TARGET_STR, TARGET_PTR));

        var expected = String.format("{\"target\":\"%s\",\"targetIdentifier\":\"%s\"}", TARGET_STR, TARGET_PTR);
        this.mockMvc
                .perform(post("/point")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

        verify(urlPointerService).shortenUrl(TARGET_URL);
        verifyNoMoreInteractions(urlPointerService);
    }

    @Test
    @SneakyThrows
    void shouldNotShortenMalformedUrl() {
        var requestJson = String.format("{\"targetUrl\":\"%s\"}", "iMaProPerLyF0rmEDurL");

        this.mockMvc
                .perform(post("/point")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Given URL is malformed"));

        verifyNoInteractions(urlPointerService);
    }
}