package com.weightloss.api.foundation.identity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Maps a verified external OIDC identity to an application account.
 *
 * The issuer and subject claim are stored together because an OIDC subject is
 * only unique within its issuing identity provider.
 */
@Entity
@Table(name = "user_accounts")
public class UserAccount {

    @Id
    private UUID id;

    @Column(nullable = false, length = 512)
    private String issuer;

    @Column(nullable = false, length = 512)
    private String subject;

    @Column(name = "display_name", length = 200)
    private String displayName;

    @Column(nullable = false, length = 32)
    private String status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected UserAccount() {
    }

    public UserAccount(UUID id, String issuer, String subject, String displayName, String status, Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.issuer = issuer;
        this.subject = subject;
        this.displayName = displayName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getSubject() {
        return subject;
    }

    public String getStatus() {
        return status;
    }
}
