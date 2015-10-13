package com.mightymerce.core.repository;

import com.mightymerce.core.domain.ChannelPost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ChannelPost entity.
 */
public interface ChannelPostRepository extends JpaRepository<ChannelPost,Long> {

    @Query("select channelPost from ChannelPost channelPost where channelPost.user.login = ?#{principal.username}")
    List<ChannelPost> findByUserIsCurrentUser();

    @Query("select channelPost from ChannelPost channelPost where channelPost.user.login = ?#{principal.username}")
    Page<ChannelPost> findByUserIsCurrentUser(Pageable pageable);

    List<ChannelPost> findByUserIdAndProductId(Long userId, Long productId);

    @Query("select channelPost from ChannelPost channelPost where channelPost.user.id = :userId and channelPost.product.id in :productIdList" )
    List<ChannelPost> findByUserIdAndProductIdIn(@Param("userId") Long userId, @Param("productIdList") List<Long> productIdList);

}
