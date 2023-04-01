package com.ue.urlshortener.application.hasher;

import java.net.URL;

public interface UrlHasher {
    String hash(URL url);
}
