package com.senhorcafe.urlshortner.url.repository;

import com.senhorcafe.urlshortner.url.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    UrlMapping findByShortCode(String shortCode);
}
