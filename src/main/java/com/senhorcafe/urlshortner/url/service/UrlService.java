package com.senhorcafe.urlshortner.url.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;

@Service
public class UrlService {
    String baseShortnedUrl = "https://openshortner.com/";

    public HashMap<String, String> url;

    public UrlService() {
        url = new HashMap<>();
    }

    public String saveUrl(String urlToSave) {
        if (urlToSave == null || urlToSave.isBlank()) return null;

        String shortCode = hash(urlToSave);
        String fullShortnedUrl = baseShortnedUrl + shortCode;
        url.putIfAbsent(shortCode, urlToSave);
        return fullShortnedUrl;
    }

    public String getUrl() {
        return url.toString();
    }

    public ResponseEntity<Void> redirect(String urlCode) {
        if (urlCode == null || urlCode.isBlank()) return null;

        String originalUrl = url.get(urlCode);
        if (originalUrl == null) return null;

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    private String hash(String urlToSave) {
        return Integer.toHexString(urlToSave.hashCode());
    }
}
