# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

Build, test, and run use the Maven wrapper (`./mvnw`); no global Maven install needed.

```bash
./mvnw spring-boot:run        # Run the app (defaults to http://localhost:8080)
./mvnw test                   # Run all tests
./mvnw test -Dtest=UrlShortnerApplicationTests#contextLoads   # Single test method
./mvnw package                # Build the executable jar into target/
./mvnw spring-boot:build-image  # Build an OCI image (Buildpacks)
```

## Architecture

Spring Boot 4 (Java 21) MVC application. It is an early-stage, mock URL shortener — there is no database; state lives only in memory and is lost on restart.

- **`UrlController`** (`url/controller`) — REST endpoints under base path `/api/v1/url/`:
  - `POST data/shorten?url=...` → returns the full shortened URL string
  - `GET shortUrl/{code}` → returns a 302 redirect (`ResponseEntity<Void>` with a `Location` header) to the original URL, or `null` if the code is unknown
  - `GET getMap` → dumps the in-memory map as a string (debug aid)
- **`UrlService`** (`url/service`) — all logic and storage. A single `HashMap<String, String>` keyed by `shortCode → originalUrl`. Short codes are derived by `hash()` = `Integer.toHexString(url.hashCode())`. Note this means two distinct URLs can collide on the same code, and `putIfAbsent` keeps the first one.

The package convention is feature-first: `com.senhorcafe.urlshortner.url.<layer>` (controller/service). New features should follow the same `url.<feature>.<layer>` grouping.

## Notes

- Lombok is available (annotation processing is configured in `pom.xml`) but not yet used in the code.
- `spring-boot-docker-compose` is on the runtime classpath and `compose.yaml` exists but defines no services yet — Spring will look for it on startup.
- `baseShortnedUrl` and the redirect targets are hardcoded in `UrlService`; the service is still mock-quality and not production-ready.
