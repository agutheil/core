package com.mightymerce.core.web.rest.mapper;

import com.mightymerce.core.domain.*;
import com.mightymerce.core.web.rest.dto.NotificationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Notification and its DTO NotificationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NotificationMapper {

    @Mapping(source = "user.id", target = "userId")
    NotificationDTO notificationToNotificationDTO(Notification notification);

    @Mapping(source = "userId", target = "user")
    Notification notificationDTOToNotification(NotificationDTO notificationDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
