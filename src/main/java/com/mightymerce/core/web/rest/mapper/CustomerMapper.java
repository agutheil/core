package com.mightymerce.core.web.rest.mapper;

import com.mightymerce.core.domain.*;
import com.mightymerce.core.web.rest.dto.CustomerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerMapper {

    @Mapping(source = "billingAddress.id", target = "billingAddressId")
    @Mapping(source = "shippingAddress.id", target = "shippingAddressId")
    @Mapping(source = "user.id", target = "userId")
    CustomerDTO customerToCustomerDTO(Customer customer);

    @Mapping(source = "billingAddressId", target = "billingAddress")
    @Mapping(source = "shippingAddressId", target = "shippingAddress")
    @Mapping(source = "userId", target = "user")
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    default CustomerAddress customerAddressFromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setId(id);
        return customerAddress;
    }

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
