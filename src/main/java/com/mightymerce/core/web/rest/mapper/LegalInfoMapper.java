package com.mightymerce.core.web.rest.mapper;

import com.mightymerce.core.domain.*;
import com.mightymerce.core.web.rest.dto.LegalInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LegalInfo and its DTO LegalInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LegalInfoMapper {

    @Mapping(source = "user.id", target = "userId")
    LegalInfoDTO legalInfoToLegalInfoDTO(LegalInfo legalInfo);

    @Mapping(source = "userId", target = "user")
    LegalInfo legalInfoDTOToLegalInfo(LegalInfoDTO legalInfoDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
