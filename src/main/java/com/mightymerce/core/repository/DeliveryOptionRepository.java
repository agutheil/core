package com.mightymerce.core.repository;

import com.mightymerce.core.domain.DeliveryOption;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DeliveryOption entity.
 */
public interface DeliveryOptionRepository extends JpaRepository<DeliveryOption,Long> {

    @Query("select deliveryOption from DeliveryOption deliveryOption where deliveryOption.user.login = ?#{principal.username}")
    List<DeliveryOption> findByUserIsCurrentUser();

    List<DeliveryOption> findByUserId(Long id);

}
