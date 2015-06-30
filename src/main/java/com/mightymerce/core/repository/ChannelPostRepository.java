package com.mightymerce.core.repository;

import com.mightymerce.core.domain.ChannelPost;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChannelPost entity.
 */
public interface ChannelPostRepository extends JpaRepository<ChannelPost,Long> {

}
