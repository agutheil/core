package com.mightymerce.core.web.rest.mapper;

import com.mightymerce.core.domain.*;
import com.mightymerce.core.web.rest.dto.CustomerOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CustomerOrder and its DTO CustomerOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerOrderMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "user.id", target = "userId")
    CustomerOrderDTO customerOrderToCustomerOrderDTO(CustomerOrder customerOrder);

    @Mapping(source = "productId", target = "product")
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "userId", target = "user")
    CustomerOrder customerOrderDTOToCustomerOrder(CustomerOrderDTO customerOrderDTO);

    default Product productFromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }

    default Customer customerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
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
