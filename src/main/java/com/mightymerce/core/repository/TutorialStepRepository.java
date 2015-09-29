package com.mightymerce.core.repository;

import com.mightymerce.core.domain.TutorialStep;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TutorialStep entity.
 */
public interface TutorialStepRepository extends JpaRepository<TutorialStep,Long> {

    @Query("select tutorialStep from TutorialStep tutorialStep where tutorialStep.user.login = ?#{principal.username}")
    List<TutorialStep> findByUserIsCurrentUser();

}
