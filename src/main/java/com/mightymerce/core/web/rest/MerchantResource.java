package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.Merchant;
import com.mightymerce.core.repository.MerchantRepository;
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
 * REST controller for managing Merchant.
 */
@RestController
@RequestMapping("/api")
public class MerchantResource {

    private final Logger log = LoggerFactory.getLogger(MerchantResource.class);

    @Inject
    private MerchantRepository merchantRepository;

    /**
     * POST  /merchants -> Create a new merchant.
     */
    @RequestMapping(value = "/merchants",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Merchant merchant) throws URISyntaxException {
        log.debug("REST request to save Merchant : {}", merchant);
        if (merchant.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new merchant cannot already have an ID").build();
        }
        merchantRepository.save(merchant);
        return ResponseEntity.created(new URI("/api/merchants/" + merchant.getId())).build();
    }

    /**
     * PUT  /merchants -> Updates an existing merchant.
     */
    @RequestMapping(value = "/merchants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Merchant merchant) throws URISyntaxException {
        log.debug("REST request to update Merchant : {}", merchant);
        if (merchant.getId() == null) {
            return create(merchant);
        }
        merchantRepository.save(merchant);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /merchants -> get all the merchants.
     */
    @RequestMapping(value = "/merchants",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Merchant>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Merchant> page = merchantRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/merchants", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /merchants/:id -> get the "id" merchant.
     */
    @RequestMapping(value = "/merchants/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Merchant> get(@PathVariable Long id) {
        log.debug("REST request to get Merchant : {}", id);
        return Optional.ofNullable(merchantRepository.findOne(id))
            .map(merchant -> new ResponseEntity<>(
                merchant,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /merchants/:id -> delete the "id" merchant.
     */
    @RequestMapping(value = "/merchants/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Merchant : {}", id);
        merchantRepository.delete(id);
    }
}
