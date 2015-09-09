package com.mightymerce.core.repository;

import com.mightymerce.core.domain.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Article entity.
 */
public interface ArticleRepository extends JpaRepository<Article,Long> {

    @Query("select article from Article article where article.user.login = ?#{principal.username}")
    List<Article> findByUserIsCurrentUser();

    @Query("select article from Article article where article.user.login = ?#{principal.username}")
    Page<Article> findByUserIsCurrentUser(Pageable pageable);

}
