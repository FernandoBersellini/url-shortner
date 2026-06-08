package com.senhorcafe.urlshortner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Externalized application config, bound from the {@code app.*} namespace in
 * application.properties (overridable per environment via env vars).
 *
 * @param baseUrl base of the generated short URLs; the short code is appended to it.
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(String baseUrl) {
}
