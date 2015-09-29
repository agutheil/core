package com.mightymerce.core.web.rest.mapper;

import com.mightymerce.core.domain.*;
import com.mightymerce.core.web.rest.dto.DeliveryOptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DeliveryOption and its DTO DeliveryOptionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeliveryOptionMapper {

    @Mapping(source = "user.id", target = "userId")
    DeliveryOptionDTO deliveryOptionToDeliveryOptionDTO(DeliveryOption deliveryOption);

    @Mapping(source = "userId", target = "user")
    DeliveryOption deliveryOptionDTOToDeliveryOption(DeliveryOptionDTO deliveryOptionDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
