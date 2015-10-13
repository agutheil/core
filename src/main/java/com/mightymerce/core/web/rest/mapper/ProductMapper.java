package com.mightymerce.core.web.rest.mapper;

import com.mightymerce.core.domain.*;
import com.mightymerce.core.web.rest.dto.ProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Product and its DTO ProductDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "deliveryOption.id", target = "deliveryOptionId")
    ProductDTO productToProductDTO(Product product);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "deliveryOptionId", target = "deliveryOption")
    Product productDTOToProduct(ProductDTO productDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    default DeliveryOption deliveryOptionFromId(Long id) {
        if (id == null) {
            return null;
        }
        DeliveryOption deliveryOption = new DeliveryOption();
        deliveryOption.setId(id);
        return deliveryOption;
    }
}
