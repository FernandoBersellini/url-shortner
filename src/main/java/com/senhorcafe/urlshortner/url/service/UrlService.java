package com.senhorcafe.urlshortner.url.service;

import org.springframework.stereotype.Service;

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
        url.putIfAbsent(fullShortnedUrl, urlToSave);
        return fullShortnedUrl;
    }

    public String getUrl() {
        return url.toString();
    }

    private String hash(String urlToSave) {
        return Integer.toHexString(urlToSave.hashCode());
    }
}
