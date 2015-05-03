package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.Article;
import com.mightymerce.core.domain.ChannelPost;
import com.mightymerce.core.repository.ChannelPostRepository;
import com.mightymerce.core.service.ChannelServiceService;
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
 * REST controller for managing ChannelPost.
 */
@RestController
@RequestMapping("/api")
public class ChannelPostResource {

    private final Logger log = LoggerFactory.getLogger(ChannelPostResource.class);

    @Inject
    private ChannelPostRepository channelPostRepository;

    @Inject
    private ChannelServiceService channelServiceService;

    /**
     * POST  /channelPosts -> Create a new channelPost.
     */
    @RequestMapping(value = "/channelPosts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ChannelPost channelPost) throws URISyntaxException {
        log.debug("REST request to save ChannelPost : {}", channelPost);
        if (channelPost.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new channelPost cannot already have an ID").build();
        }
        channelPostRepository.save(channelPost);
        updateStatus(channelPost);
        channelPost.setStatus("posted");
        channelPostRepository.save(channelPost);
        return ResponseEntity.created(new URI("/api/channelPosts/" + channelPost.getId())).build();
    }

    private void updateStatus(ChannelPost channelPost) {
        Article article = channelPost.getArticle();
        channelServiceService.updateStatus(article.toString());
    }

    /**
     * PUT  /channelPosts -> Updates an existing channelPost.
     */
    @RequestMapping(value = "/channelPosts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ChannelPost channelPost) throws URISyntaxException {
        log.debug("REST request to update ChannelPost : {}", channelPost);
        if (channelPost.getId() == null) {
            return create(channelPost);
        }
        channelPostRepository.save(channelPost);
        return ResponseEntity.ok().build();
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
        Page<ChannelPost> page = channelPostRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
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
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ChannelPost : {}", id);
        channelPostRepository.delete(id);
    }
}
