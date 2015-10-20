package com.mightymerce.core.repository;

import com.mightymerce.core.domain.LegalInfo;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LegalInfo entity.
 */
public interface LegalInfoRepository extends JpaRepository<LegalInfo,Long> {

    @Query("select legalInfo from LegalInfo legalInfo where legalInfo.user.login = ?#{principal.username}")
    List<LegalInfo> findByUserIsCurrentUser();

    List<LegalInfo> findByUserId(Long id);
}
