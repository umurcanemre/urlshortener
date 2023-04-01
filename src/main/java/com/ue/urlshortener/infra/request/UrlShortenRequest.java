package com.ue.urlshortener.infra.request;

import java.net.URL;

public record UrlShortenRequest(URL targetUrl) {
}
