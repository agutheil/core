package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.Article;
import com.mightymerce.core.domain.ChannelPost;
import com.mightymerce.core.domain.enumeration.PublicationStatus;
import com.mightymerce.core.repository.ChannelPostRepository;
import com.mightymerce.core.repository.UserRepository;
import com.mightymerce.core.security.SecurityUtils;
import com.mightymerce.core.service.ChannelPostService;
import com.mightymerce.core.web.rest.util.HeaderUtil;
import com.mightymerce.core.web.rest.util.PaginationUtil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
 * REST controller for managing ChannelPost.
 */
@RestController
@RequestMapping("/api")
public class ChannelPostResource {

    private final Logger log = LoggerFactory.getLogger(ChannelPostResource.class);

    @Inject
    private ChannelPostRepository channelPostRepository;

    @Inject
    private ChannelPostService channelPostService;
    
    @Inject
    private UserRepository userRepository;

    /**
     * POST  /channelPosts -> Create a new channelPost.
     */
    @RequestMapping(value = "/channelPosts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChannelPost> create(@RequestBody ChannelPost channelPost) throws URISyntaxException {
        log.debug("REST request to save ChannelPost : {}", channelPost);
        if (channelPost.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new channelPost cannot already have an ID").body(null);
        }
        channelPost.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        ChannelPost result = channelPostRepository.save(channelPost);
        String externalPostKey = channelPostService.updateStatus(result); 
        channelPost.setExternalPostKey(externalPostKey);
        channelPost.setStatus(PublicationStatus.published);
        channelPost.setPublicationDate(DateTime.now(DateTimeZone.UTC));
        result = channelPostRepository.save(result);
        return ResponseEntity.created(new URI("/api/channelPosts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("channelPost", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /channelPosts -> Updates an existing channelPost.
     */
    @RequestMapping(value = "/channelPosts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChannelPost> update(@RequestBody ChannelPost channelPost) throws URISyntaxException {
        log.debug("REST request to update ChannelPost : {}", channelPost);
        if (channelPost.getId() == null) {
            return create(channelPost);
        }
        channelPost.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        ChannelPost result = channelPostRepository.save(channelPost);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("channelPost", channelPost.getId().toString()))
                .body(result);
    }

    /**
     * GET  /channelPosts -> get all the channelPosts.
     */
    @RequestMapping(value = "/channelPosts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChannelPost>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<ChannelPost> page = channelPostRepository.findByUserIsCurrentUser(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/channelPosts", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /channelPosts/:id -> get the "id" channelPost.
     */
    @RequestMapping(value = "/channelPosts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChannelPost> get(@PathVariable Long id) {
        log.debug("REST request to get ChannelPost : {}", id);
        return Optional.ofNullable(channelPostRepository.findOne(id))
            .map(channelPost -> new ResponseEntity<>(
                channelPost,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /channelPosts/:id -> delete the "id" channelPost.
     */
    @RequestMapping(value = "/channelPosts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete ChannelPost : {}", id);
        channelPostRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("channelPost", id.toString())).build();
    }
}
