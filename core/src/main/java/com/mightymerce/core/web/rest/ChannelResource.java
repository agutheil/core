package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.Channel;
import com.mightymerce.core.repository.ChannelRepository;
import com.mightymerce.core.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
    public ResponseEntity<Void> create(@Valid @RequestBody Channel channel) throws URISyntaxException {
        log.debug("REST request to save Channel : {}", channel);
        if (channel.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new channel cannot already have an ID").build();
        }
        channelRepository.save(channel);
        return ResponseEntity.created(new URI("/api/channels/" + channel.getId())).build();
    }

    /**
     * PUT  /channels -> Updates an existing channel.
     */
    @RequestMapping(value = "/channels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Channel channel) throws URISyntaxException {
        log.debug("REST request to update Channel : {}", channel);
        if (channel.getId() == null) {
            return create(channel);
        }
        channelRepository.save(channel);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /channels -> get all the channels.
     */
    @RequestMapping(value = "/channels",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Channel>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Channel> page = channelRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/channels", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /channels/:id -> get the "id" channel.
     */
    @RequestMapping(value = "/channels/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Channel> get(@PathVariable Long id) {
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
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Channel : {}", id);
        channelRepository.delete(id);
    }
}
