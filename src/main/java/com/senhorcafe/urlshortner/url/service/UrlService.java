package com.senhorcafe.urlshortner.url.service;

import com.senhorcafe.urlshortner.config.AppProperties;
import com.senhorcafe.urlshortner.config.CacheConfig;
import com.senhorcafe.urlshortner.url.entity.UrlMapping;
import com.senhorcafe.urlshortner.url.repository.UrlMappingRepository;
import com.senhorcafe.urlshortner.user.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@Service
public class UrlService {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String baseShortnedUrl;
    private final UrlMappingRepository urlMappingRepository;


    public UrlService(AppProperties appProperties, UrlMappingRepository urlMappingRepository) {
        this.baseShortnedUrl = appProperties.baseUrl();
        this.urlMappingRepository = urlMappingRepository;
    }

    public String saveUrl(String urlToSave) {
        if (urlToSave == null || urlToSave.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A URL to shorten is required");
        }

        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        // Persist first so the DB assigns the unique id, then derive the short
        // code from it. Using the DB id (instead of an in-memory counter) keeps
        // codes unique and monotonic across application restarts.
        UrlMapping mapping = new UrlMapping();
        mapping.setLongUrl(urlToSave);
        mapping.setUser(owner);
        mapping = urlMappingRepository.save(mapping);

        String shortCode = encode(mapping.getId());
        mapping.setShortCode(shortCode);
        urlMappingRepository.save(mapping);

        return baseShortnedUrl + shortCode;
    }

    @Cacheable(cacheNames = CacheConfig.URL_MAPPINGS, key = "#urlCode")
    public ResponseEntity<Void> redirect(String urlCode) {
        if (urlCode == null || urlCode.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A short code is required");
        }

        UrlMapping mapping = urlMappingRepository.findByShortCode(urlCode);
        if (mapping == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No URL found for short code '" + urlCode + "'");
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(mapping.getLongUrl()))
                .build();
    }

    /**
     * Removes a mapping and evicts its cached redirect, so a deleted/expired code
     * is no longer served from the cache. The key must match the one used by
     * {@link #redirect} (the short code) so the right entry is removed.
     */
    @CacheEvict(cacheNames = CacheConfig.URL_MAPPINGS, key = "#shortCode")
    public void deleteByShortCode(String shortCode) {
        UrlMapping mapping = urlMappingRepository.findByShortCode(shortCode);
        if (mapping != null) {
            urlMappingRepository.delete(mapping);
        }
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
