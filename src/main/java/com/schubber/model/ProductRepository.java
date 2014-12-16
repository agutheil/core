package com.schubber.model;

import java.util.List;

/**
 * Created by agutheil on 16.12.14.
 */
public interface ProductRepository {
    List<Product> findAll();
    Product findById(Long id);
}
