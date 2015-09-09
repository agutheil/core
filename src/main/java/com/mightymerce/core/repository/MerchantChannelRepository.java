package com.mightymerce.core.repository;

import com.mightymerce.core.domain.MerchantChannel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MerchantChannel entity.
 */
public interface MerchantChannelRepository extends JpaRepository<MerchantChannel,Long> {

    @Query("select merchantChannel from MerchantChannel merchantChannel where merchantChannel.user.login = ?#{principal.username}")
    List<MerchantChannel> findByUserIsCurrentUser();
    
    @Query("select merchantChannel from MerchantChannel merchantChannel where merchantChannel.user.login = ?#{principal.username}")
    Page<MerchantChannel> findByUserIsCurrentUser(Pageable pageabel);

}
