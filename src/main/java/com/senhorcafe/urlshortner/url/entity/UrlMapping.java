package com.senhorcafe.urlshortner.url.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "url")
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "short_code")
    private String shortCode;

    @Column(name = "long_url")
    private String longUrl;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = true)
    private LocalDateTime expiresAt;

    @Column(name = "owner_id", nullable = true)
    private Integer ownerId;
}
