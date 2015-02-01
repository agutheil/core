package com.schubber.schubber.repository;

import com.schubber.schubber.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Product entity.
 */
public interface ProductRepository extends MongoRepository<Product,String>{

}
