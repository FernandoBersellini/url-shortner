package com.senhorcafe.urlshortner.url.service;

import com.senhorcafe.urlshortner.config.AppProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UrlService {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final String baseShortnedUrl;

    private final HashMap<String, String> url;

    // Source of unique IDs; each new URL gets the next value, encoded to base62.
    private final AtomicLong counter = new AtomicLong(1);

    public UrlService(AppProperties appProperties) {
        this.baseShortnedUrl = appProperties.baseUrl();
        this.url = new HashMap<>();
    }

    public String saveUrl(String urlToSave) {
        if (urlToSave == null || urlToSave.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A URL to shorten is required");
        }

        String shortCode = encode(counter.getAndIncrement());
        String fullShortnedUrl = baseShortnedUrl + shortCode;
        url.put(shortCode, urlToSave);
        return fullShortnedUrl;
    }

    public ResponseEntity<Void> redirect(String urlCode) {
        if (urlCode == null || urlCode.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A short code is required");
        }

        String originalUrl = url.get(urlCode);
        if (originalUrl == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No URL found for short code '" + urlCode + "'");
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    private String encode(long id) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(BASE62.charAt((int) (id % 62)));
            id /= 62;
        } while (id > 0);
        return sb.reverse().toString();
    }
}
