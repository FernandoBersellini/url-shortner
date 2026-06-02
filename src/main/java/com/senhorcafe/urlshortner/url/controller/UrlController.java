package com.senhorcafe.urlshortner.url.controller;

import com.senhorcafe.urlshortner.url.service.UrlService;
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

    @GetMapping("shortUrl")
    public String get()  {
        return urlService.getUrl();
    }
}
