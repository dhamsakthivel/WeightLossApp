package com.weightloss.api.foundation.identity;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    Optional<UserAccount> findByIssuerAndSubjectAndStatus(String issuer, String subject, String status);
}
