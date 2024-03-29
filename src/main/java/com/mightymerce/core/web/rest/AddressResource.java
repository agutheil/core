package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.Address;
import com.mightymerce.core.repository.AddressRepository;
import com.mightymerce.core.repository.UserRepository;
import com.mightymerce.core.security.SecurityUtils;
import com.mightymerce.core.web.rest.util.HeaderUtil;
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
 * REST controller for managing Address.
 */
@RestController
@RequestMapping("/api")
public class AddressResource {

    private final Logger log = LoggerFactory.getLogger(AddressResource.class);

    @Inject
    private AddressRepository addressRepository;
    
    @Inject
    private UserRepository userRepository;

    /**
     * POST  /addresss -> Create a new address.
     */
    @RequestMapping(value = "/addresss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Address> create(@Valid @RequestBody Address address) throws URISyntaxException {
        log.debug("REST request to save Address : {}", address);
        if (address.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new address cannot already have an ID").body(null);
        }
        address.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        Address result = addressRepository.save(address);
        return ResponseEntity.created(new URI("/api/addresss/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("address", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /addresss -> Updates an existing address.
     */
    @RequestMapping(value = "/addresss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Address> update(@Valid @RequestBody Address address) throws URISyntaxException {
        log.debug("REST request to update Address : {}", address);
        if (address.getId() == null) {
            return create(address);
        }
        address.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        Address result = addressRepository.save(address);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("address", address.getId().toString()))
                .body(result);
    }

    /**
     * GET  /addresss -> get all the addresss.
     */
    @RequestMapping(value = "/addresss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Address>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Address> page = addressRepository.findByUserIsCurrentUser(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/addresss", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /addresss/:id -> get the "id" address.
     */
    @RequestMapping(value = "/addresss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Address> get(@PathVariable Long id) {
        log.debug("REST request to get Address : {}", id);
        return Optional.ofNullable(addressRepository.findOne(id))
            .map(address -> new ResponseEntity<>(
                address,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /addresss/:id -> delete the "id" address.
     */
    @RequestMapping(value = "/addresss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Address : {}", id);
        addressRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("address", id.toString())).build();
    }
}
