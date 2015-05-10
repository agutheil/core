package com.mightymerce.core.repository;

import com.mightymerce.core.domain.SocialCart;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SocialCart entity.
 */
public interface SocialCartRepository extends JpaRepository<SocialCart,Long> {

}
