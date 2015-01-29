package com.schubber.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by agutheil on 29.01.15.
 */
public interface AccountRepository extends JpaRepository<Account, Long>{
    Optional<Account> findByUsername(String username);
}
