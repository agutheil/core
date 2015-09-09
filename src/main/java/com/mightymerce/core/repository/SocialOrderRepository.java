package com.mightymerce.core.repository;

import com.mightymerce.core.domain.SocialOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SocialOrder entity.
 */
public interface SocialOrderRepository extends JpaRepository<SocialOrder,Long> {

    @Query("select socialOrder from SocialOrder socialOrder where socialOrder.user.login = ?#{principal.username}")
    List<SocialOrder> findByUserIsCurrentUser();
    
    @Query("select socialOrder from SocialOrder socialOrder where socialOrder.user.login = ?#{principal.username}")
    Page<SocialOrder> findByUserIsCurrentUser(Pageable pageable);

}
