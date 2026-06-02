package com.senhorcafe.urlshortner.url.controller;

import com.senhorcafe.urlshortner.url.service.UrlService;
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
    public String post(@RequestParam String url) {
        return urlService.saveUrl(url);
    }

    @GetMapping("shortUrl/{code}")
    public ResponseEntity<Void> get(@PathVariable String code)  {
        return urlService.redirect(code);
    }

    @GetMapping("getMap")
    public String getAllUrls() {
        return this.urlService.getUrl();

    }
}
