package com.schubber.model;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by agutheil on 16.12.14.
 */
@Repository
public class RuntimeProductRepository implements ProductRepository {

    private List<Product> products;

    public RuntimeProductRepository(){
        products = new ArrayList<>();
        for(long i = 0; i < 100; i++) {
            products.add(createProduct(i));
        }
    }

    private Product createProduct(long i) {
        return new Product(i, "Title "+i, BigDecimal.valueOf(19.99), Currency.EUR, 100L*(i+1));
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Product findById(Long id) {
        return products.stream().filter(p -> p.getId().longValue()==id.longValue()).findFirst().get();
    }
}
