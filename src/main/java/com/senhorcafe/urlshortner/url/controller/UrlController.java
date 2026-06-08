package com.senhorcafe.urlshortner.url.controller;

import com.senhorcafe.urlshortner.url.dto.ShortenRequest;
import com.senhorcafe.urlshortner.url.dto.ShortenResponse;
import com.senhorcafe.urlshortner.url.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/url/")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("data/shorten")
    public ShortenResponse post(@Valid @RequestBody ShortenRequest shortenRequest) {
        return new ShortenResponse(urlService.saveUrl(shortenRequest.url()));
    }

    @GetMapping("shortUrl/{shortCode}")
    public ResponseEntity<Void> get(@PathVariable String shortCode)  {
        return urlService.redirect(shortCode);
    }
}

