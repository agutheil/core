package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.LineItem;
import com.mightymerce.core.repository.LineItemRepository;
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
 * REST controller for managing LineItem.
 */
@RestController
@RequestMapping("/api")
public class LineItemResource {

    private final Logger log = LoggerFactory.getLogger(LineItemResource.class);

    @Inject
    private LineItemRepository lineItemRepository;

    /**
     * POST  /lineItems -> Create a new lineItem.
     */
    @RequestMapping(value = "/lineItems",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody LineItem lineItem) throws URISyntaxException {
        log.debug("REST request to save LineItem : {}", lineItem);
        if (lineItem.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new lineItem cannot already have an ID").build();
        }
        lineItemRepository.save(lineItem);
        return ResponseEntity.created(new URI("/api/lineItems/" + lineItem.getId())).build();
    }

    /**
     * PUT  /lineItems -> Updates an existing lineItem.
     */
    @RequestMapping(value = "/lineItems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody LineItem lineItem) throws URISyntaxException {
        log.debug("REST request to update LineItem : {}", lineItem);
        if (lineItem.getId() == null) {
            return create(lineItem);
        }
        lineItemRepository.save(lineItem);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /lineItems -> get all the lineItems.
     */
    @RequestMapping(value = "/lineItems",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LineItem>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<LineItem> page = lineItemRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lineItems", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lineItems/:id -> get the "id" lineItem.
     */
    @RequestMapping(value = "/lineItems/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LineItem> get(@PathVariable Long id) {
        log.debug("REST request to get LineItem : {}", id);
        return Optional.ofNullable(lineItemRepository.findOne(id))
            .map(lineItem -> new ResponseEntity<>(
                lineItem,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lineItems/:id -> delete the "id" lineItem.
     */
    @RequestMapping(value = "/lineItems/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete LineItem : {}", id);
        lineItemRepository.delete(id);
    }
}
