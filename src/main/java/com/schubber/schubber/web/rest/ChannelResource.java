package com.schubber.schubber.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schubber.schubber.domain.Channel;
import com.schubber.schubber.repository.ChannelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Channel.
 */
@RestController
@RequestMapping("/api")
public class ChannelResource {

    private final Logger log = LoggerFactory.getLogger(ChannelResource.class);

    @Inject
    private ChannelRepository channelRepository;

    /**
     * POST  /channels -> Create a new channel.
     */
    @RequestMapping(value = "/channels",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Channel channel) {
        log.debug("REST request to save Channel : {}", channel);
        channelRepository.save(channel);
    }

    /**
     * GET  /channels -> get all the channels.
     */
    @RequestMapping(value = "/channels",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Channel> getAll() {
        log.debug("REST request to get all Channels");
        return channelRepository.findAll();
    }

    /**
     * GET  /channels/:id -> get the "id" channel.
     */
    @RequestMapping(value = "/channels/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Channel> get(@PathVariable String id) {
        log.debug("REST request to get Channel : {}", id);
        return Optional.ofNullable(channelRepository.findOne(id))
            .map(channel -> new ResponseEntity<>(
                channel,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /channels/:id -> delete the "id" channel.
     */
    @RequestMapping(value = "/channels/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete Channel : {}", id);
        channelRepository.delete(id);
    }
}
