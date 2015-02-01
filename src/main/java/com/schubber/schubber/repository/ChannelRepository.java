package com.schubber.schubber.repository;

import com.schubber.schubber.domain.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Channel entity.
 */
public interface ChannelRepository extends MongoRepository<Channel,String>{

}
