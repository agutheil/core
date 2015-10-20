package com.mightymerce.core.repository;

import com.mightymerce.core.domain.CustomerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerOrder entity.
 */
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder,Long> {

    @Query("select customerOrder from CustomerOrder customerOrder where customerOrder.user.login = ?#{principal.username}")
    List<CustomerOrder> findByUserIsCurrentUser();

    Page<CustomerOrder> findByUserId(Long id, Pageable pageable);

}
