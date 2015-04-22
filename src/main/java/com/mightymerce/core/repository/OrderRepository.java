package com.mightymerce.core.repository;

import com.mightymerce.core.domain.Order;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Order entity.
 */
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("select order from Order order where order.user.login = ?#{principal.username}")
    List<Order> findAllForCurrentUser();

}
