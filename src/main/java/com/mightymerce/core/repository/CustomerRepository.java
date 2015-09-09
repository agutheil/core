package com.mightymerce.core.repository;

import com.mightymerce.core.domain.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Customer entity.
 */
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query("select customer from Customer customer where customer.user.login = ?#{principal.username}")
    List<Customer> findByUserIsCurrentUser();
    
    @Query("select customer from Customer customer where customer.user.login = ?#{principal.username}")
    Page<Customer> findByUserIsCurrentUser(Pageable page);

}
