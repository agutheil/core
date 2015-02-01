package com.schubber.schubber.repository;

import com.schubber.schubber.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Order entity.
 */
public interface OrderRepository extends MongoRepository<Order,String>{

}
