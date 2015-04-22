package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.SocialCart;
import com.mightymerce.core.repository.SocialCartRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SocialCart.
 */
@RestController
@RequestMapping("/api")
public class SocialCartResource {

    private final Logger log = LoggerFactory.getLogger(SocialCartResource.class);

    @Inject
    private SocialCartRepository socialCartRepository;

    /**
     * POST  /socialCarts -> Create a new socialCart.
     */
    @RequestMapping(value = "/socialCarts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody SocialCart socialCart) throws URISyntaxException {
        log.debug("REST request to save SocialCart : {}", socialCart);
        if (socialCart.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new socialCart cannot already have an ID").build();
        }
        socialCartRepository.save(socialCart);
        return ResponseEntity.created(new URI("/api/socialCarts/" + socialCart.getId())).build();
    }

    /**
     * PUT  /socialCarts -> Updates an existing socialCart.
     */
    @RequestMapping(value = "/socialCarts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody SocialCart socialCart) throws URISyntaxException {
        log.debug("REST request to update SocialCart : {}", socialCart);
        if (socialCart.getId() == null) {
            return create(socialCart);
        }
        socialCartRepository.save(socialCart);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /socialCarts -> get all the socialCarts.
     */
    @RequestMapping(value = "/socialCarts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SocialCart>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<SocialCart> page = socialCartRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/socialCarts", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /socialCarts/:id -> get the "id" socialCart.
     */
    @RequestMapping(value = "/socialCarts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SocialCart> get(@PathVariable Long id) {
        log.debug("REST request to get SocialCart : {}", id);
        return Optional.ofNullable(socialCartRepository.findOne(id))
            .map(socialCart -> new ResponseEntity<>(
                socialCart,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /socialCarts/:id -> delete the "id" socialCart.
     */
    @RequestMapping(value = "/socialCarts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete SocialCart : {}", id);
        socialCartRepository.delete(id);
    }
}
