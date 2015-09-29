package com.mightymerce.core.web.rest.mapper;

import com.mightymerce.core.domain.*;
import com.mightymerce.core.web.rest.dto.TutorialStepDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TutorialStep and its DTO TutorialStepDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TutorialStepMapper {

    @Mapping(source = "user.id", target = "userId")
    TutorialStepDTO tutorialStepToTutorialStepDTO(TutorialStep tutorialStep);

    @Mapping(source = "userId", target = "user")
    TutorialStep tutorialStepDTOToTutorialStep(TutorialStepDTO tutorialStepDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
