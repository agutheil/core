package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.CustomerChannel;
import com.mightymerce.core.repository.CustomerChannelRepository;
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
 * REST controller for managing CustomerChannel.
 */
@RestController
@RequestMapping("/api")
public class CustomerChannelResource {

    private final Logger log = LoggerFactory.getLogger(CustomerChannelResource.class);

    @Inject
    private CustomerChannelRepository customerChannelRepository;

    /**
     * POST  /customerChannels -> Create a new customerChannel.
     */
    @RequestMapping(value = "/customerChannels",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody CustomerChannel customerChannel) throws URISyntaxException {
        log.debug("REST request to save CustomerChannel : {}", customerChannel);
        if (customerChannel.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new customerChannel cannot already have an ID").build();
        }
        customerChannelRepository.save(customerChannel);
        return ResponseEntity.created(new URI("/api/customerChannels/" + customerChannel.getId())).build();
    }

    /**
     * PUT  /customerChannels -> Updates an existing customerChannel.
     */
    @RequestMapping(value = "/customerChannels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody CustomerChannel customerChannel) throws URISyntaxException {
        log.debug("REST request to update CustomerChannel : {}", customerChannel);
        if (customerChannel.getId() == null) {
            return create(customerChannel);
        }
        customerChannelRepository.save(customerChannel);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /customerChannels -> get all the customerChannels.
     */
    @RequestMapping(value = "/customerChannels",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CustomerChannel>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<CustomerChannel> page = customerChannelRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customerChannels", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /customerChannels/:id -> get the "id" customerChannel.
     */
    @RequestMapping(value = "/customerChannels/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerChannel> get(@PathVariable Long id) {
        log.debug("REST request to get CustomerChannel : {}", id);
        return Optional.ofNullable(customerChannelRepository.findOne(id))
            .map(customerChannel -> new ResponseEntity<>(
                customerChannel,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customerChannels/:id -> delete the "id" customerChannel.
     */
    @RequestMapping(value = "/customerChannels/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete CustomerChannel : {}", id);
        customerChannelRepository.delete(id);
    }
}
