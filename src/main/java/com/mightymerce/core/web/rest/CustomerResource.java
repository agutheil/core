package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.Customer;
import com.mightymerce.core.repository.CustomerRepository;
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
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/api")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    @Inject
    private CustomerRepository customerRepository;
    
    @Inject
    private UserRepository userRepository;

    /**
     * POST  /customers -> Create a new customer.
     */
    @RequestMapping(value = "/customers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Customer> create(@Valid @RequestBody Customer customer) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customer);
        if (customer.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new customer cannot already have an ID").body(null);
        }
        customer.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        Customer result = customerRepository.save(customer);
        return ResponseEntity.created(new URI("/api/customers/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("customer", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /customers -> Updates an existing customer.
     */
    @RequestMapping(value = "/customers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Customer> update(@Valid @RequestBody Customer customer) throws URISyntaxException {
        log.debug("REST request to update Customer : {}", customer);
        if (customer.getId() == null) {
            return create(customer);
        }
        customer.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        Customer result = customerRepository.save(customer);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("customer", customer.getId().toString()))
                .body(result);
    }

    /**
     * GET  /customers -> get all the customers.
     */
    @RequestMapping(value = "/customers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Customer>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Customer> page = customerRepository.findByUserIsCurrentUser(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customers", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /customers/:id -> get the "id" customer.
     */
    @RequestMapping(value = "/customers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Customer> get(@PathVariable Long id) {
        log.debug("REST request to get Customer : {}", id);
        return Optional.ofNullable(customerRepository.findOne(id))
            .map(customer -> new ResponseEntity<>(
                customer,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customers/:id -> delete the "id" customer.
     */
    @RequestMapping(value = "/customers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Customer : {}", id);
        customerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customer", id.toString())).build();
    }
}
