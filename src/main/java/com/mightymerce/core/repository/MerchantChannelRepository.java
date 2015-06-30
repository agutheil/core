package com.mightymerce.core.repository;

import com.mightymerce.core.domain.MerchantChannel;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MerchantChannel entity.
 */
public interface MerchantChannelRepository extends JpaRepository<MerchantChannel,Long> {

}
