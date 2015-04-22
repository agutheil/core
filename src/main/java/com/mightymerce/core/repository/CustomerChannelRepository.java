package com.mightymerce.core.repository;

import com.mightymerce.core.domain.CustomerChannel;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerChannel entity.
 */
public interface CustomerChannelRepository extends JpaRepository<CustomerChannel,Long> {

}
