package com.schubber.schubber.repository;

import com.schubber.schubber.domain.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Customer entity.
 */
public interface CustomerRepository extends MongoRepository<Customer,String>{

}
