package com.senhorcafe.urlshortner.url.dto;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record ShortenRequest(
    @NotBlank(message = "A valid url is required")
    @URL
    @Size(min = 1, max = 100)
    String url
) {}
