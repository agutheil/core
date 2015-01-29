package com.schubber.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by agutheil on 16.12.14.
 */
public interface ProductRepository extends JpaRepository<Product, Long>{
    Collection<Product> findByAccountUsername(String username);
}
