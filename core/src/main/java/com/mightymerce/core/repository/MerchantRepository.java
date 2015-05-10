package com.mightymerce.core.repository;

import com.mightymerce.core.domain.Merchant;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Merchant entity.
 */
public interface MerchantRepository extends JpaRepository<Merchant,Long> {

}
