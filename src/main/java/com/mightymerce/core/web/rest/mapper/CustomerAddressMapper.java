package com.mightymerce.core.web.rest.mapper;

import com.mightymerce.core.domain.*;
import com.mightymerce.core.web.rest.dto.CustomerAddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CustomerAddress and its DTO CustomerAddressDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerAddressMapper {

    @Mapping(source = "user.id", target = "userId")
    CustomerAddressDTO customerAddressToCustomerAddressDTO(CustomerAddress customerAddress);

    @Mapping(source = "userId", target = "user")
    CustomerAddress customerAddressDTOToCustomerAddress(CustomerAddressDTO customerAddressDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
