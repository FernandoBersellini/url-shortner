package com.senhorcafe.urlshortner.config;

public record JwtProperties(
   String secret,
   long expirationMs
) {}
