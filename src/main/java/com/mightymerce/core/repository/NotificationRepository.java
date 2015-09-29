package com.mightymerce.core.repository;

import com.mightymerce.core.domain.Notification;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Notification entity.
 */
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    @Query("select notification from Notification notification where notification.user.login = ?#{principal.username}")
    List<Notification> findByUserIsCurrentUser();

}
