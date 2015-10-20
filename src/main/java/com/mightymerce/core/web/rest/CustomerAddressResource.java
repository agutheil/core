package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.CustomerAddress;
import com.mightymerce.core.domain.User;
import com.mightymerce.core.repository.CustomerAddressRepository;
import com.mightymerce.core.repository.UserRepository;
import com.mightymerce.core.security.SecurityUtils;
import com.mightymerce.core.web.rest.util.HeaderUtil;
import com.mightymerce.core.web.rest.util.PaginationUtil;
import com.mightymerce.core.web.rest.dto.CustomerAddressDTO;
import com.mightymerce.core.web.rest.mapper.CustomerAddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing CustomerAddress.
 */
@RestController
@RequestMapping("/api")
public class CustomerAddressResource {

    private final Logger log = LoggerFactory.getLogger(CustomerAddressResource.class);

    @Inject
    private CustomerAddressRepository customerAddressRepository;

    @Inject
    private CustomerAddressMapper customerAddressMapper;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /customerAddresss -> Create a new customerAddress.
     */
    @RequestMapping(value = "/customerAddresss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerAddressDTO> create(@Valid @RequestBody CustomerAddressDTO customerAddressDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerAddress : {}", customerAddressDTO);
        if (customerAddressDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new customerAddress cannot already have an ID").body(null);
        }
        CustomerAddress customerAddress = customerAddressMapper.customerAddressDTOToCustomerAddress(customerAddressDTO);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        customerAddress.setUser(currentUser.get());
        CustomerAddress result = customerAddressRepository.save(customerAddress);
        return ResponseEntity.created(new URI("/api/customerAddresss/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("customerAddress", result.getId().toString()))
                .body(customerAddressMapper.customerAddressToCustomerAddressDTO(result));
    }

    /**
     * PUT  /customerAddresss -> Updates an existing customerAddress.
     */
    @RequestMapping(value = "/customerAddresss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerAddressDTO> update(@Valid @RequestBody CustomerAddressDTO customerAddressDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerAddress : {}", customerAddressDTO);
        if (customerAddressDTO.getId() == null) {
            return create(customerAddressDTO);
        }
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(!currentUser.get().getId().equals(customerAddressDTO.getUserId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        CustomerAddress customerAddress = customerAddressMapper.customerAddressDTOToCustomerAddress(customerAddressDTO);
        CustomerAddress result = customerAddressRepository.save(customerAddress);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("customerAddress", customerAddressDTO.getId().toString()))
                .body(customerAddressMapper.customerAddressToCustomerAddressDTO(result));
    }

    /**
     * GET  /customerAddresss -> get all the customerAddresss.
     */
    @RequestMapping(value = "/customerAddresss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CustomerAddressDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        Page<CustomerAddress> page = customerAddressRepository.findByUserId(currentUser.get().getId(), PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customerAddresss", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(customerAddressMapper::customerAddressToCustomerAddressDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /customerAddresss/:id -> get the "id" customerAddress.
     */
    @RequestMapping(value = "/customerAddresss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerAddressDTO> get(@PathVariable Long id) {
        log.debug("REST request to get CustomerAddress : {}", id);
        CustomerAddress customerAddress = customerAddressRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(customerAddress.getUser() == null || !currentUser.get().getId().equals(customerAddress.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        return Optional.ofNullable(customerAddress)
            .map(customerAddressMapper::customerAddressToCustomerAddressDTO)
            .map(customerAddressDTO -> new ResponseEntity<>(
                customerAddressDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customerAddresss/:id -> delete the "id" customerAddress.
     */
    @RequestMapping(value = "/customerAddresss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete CustomerAddress : {}", id);
        CustomerAddress customerAddress = customerAddressRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(customerAddress.getUser() == null || !currentUser.get().getId().equals(customerAddress.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        customerAddressRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customerAddress", id.toString())).build();
    }
}
