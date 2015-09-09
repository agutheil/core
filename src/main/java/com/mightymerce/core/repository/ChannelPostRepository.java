package com.mightymerce.core.repository;

import com.mightymerce.core.domain.ChannelPost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChannelPost entity.
 */
public interface ChannelPostRepository extends JpaRepository<ChannelPost,Long> {

    @Query("select channelPost from ChannelPost channelPost where channelPost.user.login = ?#{principal.username}")
    List<ChannelPost> findByUserIsCurrentUser();
    
    @Query("select channelPost from ChannelPost channelPost where channelPost.user.login = ?#{principal.username}")
    Page<ChannelPost> findByUserIsCurrentUser(Pageable pageable);

}
