package com.mightymerce.core.repository;

import com.mightymerce.core.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Customer entity.
 */
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query("select customer from Customer customer where customer.user.login = ?#{principal.username}")
    List<Customer> findByUserIsCurrentUser();

    Page<Customer> findByUserId(Long id, Pageable pageable);

    @Query("select customer from Customer customer where customer.user.id = :userId and customer.id in :customerIdList" )
    List<Customer> findByUserIdAndCustomerIdIn(@Param("userId") Long userId, @Param("customerIdList") List<Long> customerIdList);


}
