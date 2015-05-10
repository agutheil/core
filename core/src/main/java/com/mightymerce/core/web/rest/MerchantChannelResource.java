package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.MerchantChannel;
import com.mightymerce.core.repository.MerchantChannelRepository;
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
 * REST controller for managing MerchantChannel.
 */
@RestController
@RequestMapping("/api")
public class MerchantChannelResource {

    private final Logger log = LoggerFactory.getLogger(MerchantChannelResource.class);

    @Inject
    private MerchantChannelRepository merchantChannelRepository;

    /**
     * POST  /merchantChannels -> Create a new merchantChannel.
     */
    @RequestMapping(value = "/merchantChannels",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody MerchantChannel merchantChannel) throws URISyntaxException {
        log.debug("REST request to save MerchantChannel : {}", merchantChannel);
        if (merchantChannel.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new merchantChannel cannot already have an ID").build();
        }
        merchantChannelRepository.save(merchantChannel);
        return ResponseEntity.created(new URI("/api/merchantChannels/" + merchantChannel.getId())).build();
    }

    /**
     * PUT  /merchantChannels -> Updates an existing merchantChannel.
     */
    @RequestMapping(value = "/merchantChannels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody MerchantChannel merchantChannel) throws URISyntaxException {
        log.debug("REST request to update MerchantChannel : {}", merchantChannel);
        if (merchantChannel.getId() == null) {
            return create(merchantChannel);
        }
        merchantChannelRepository.save(merchantChannel);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /merchantChannels -> get all the merchantChannels.
     */
    @RequestMapping(value = "/merchantChannels",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MerchantChannel>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<MerchantChannel> page = merchantChannelRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/merchantChannels", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /merchantChannels/:id -> get the "id" merchantChannel.
     */
    @RequestMapping(value = "/merchantChannels/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MerchantChannel> get(@PathVariable Long id) {
        log.debug("REST request to get MerchantChannel : {}", id);
        return Optional.ofNullable(merchantChannelRepository.findOne(id))
            .map(merchantChannel -> new ResponseEntity<>(
                merchantChannel,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /merchantChannels/:id -> delete the "id" merchantChannel.
     */
    @RequestMapping(value = "/merchantChannels/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete MerchantChannel : {}", id);
        merchantChannelRepository.delete(id);
    }
}
