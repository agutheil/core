package com.mightymerce.core.repository;

import com.mightymerce.core.domain.CustomerAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerAddress entity.
 */
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress,Long> {

    @Query("select customerAddress from CustomerAddress customerAddress where customerAddress.user.login = ?#{principal.username}")
    List<CustomerAddress> findByUserIsCurrentUser();

    Page<CustomerAddress> findByUserId(Long id, Pageable pageable);

}
