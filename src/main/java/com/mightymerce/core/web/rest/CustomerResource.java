package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.Customer;
import com.mightymerce.core.domain.User;
import com.mightymerce.core.repository.CustomerRepository;
import com.mightymerce.core.repository.UserRepository;
import com.mightymerce.core.security.SecurityUtils;
import com.mightymerce.core.web.rest.util.HeaderUtil;
import com.mightymerce.core.web.rest.util.PaginationUtil;
import com.mightymerce.core.web.rest.dto.CustomerDTO;
import com.mightymerce.core.web.rest.mapper.CustomerMapper;
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
import java.util.*;
import java.util.stream.Collectors;

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
    private CustomerMapper customerMapper;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /customers -> Create a new customer.
     */
    @RequestMapping(value = "/customers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerDTO> create(@Valid @RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customerDTO);
        if (customerDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new customer cannot already have an ID").body(null);
        }
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        customer.setUser(currentUser.get());
        Customer result = customerRepository.save(customer);
        return ResponseEntity.created(new URI("/api/customers/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("customer", result.getId().toString()))
                .body(customerMapper.customerToCustomerDTO(result));
    }

    /**
     * PUT  /customers -> Updates an existing customer.
     */
    @RequestMapping(value = "/customers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerDTO> update(@Valid @RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to update Customer : {}", customerDTO);
        if (customerDTO.getId() == null) {
            return create(customerDTO);
        }
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(!currentUser.get().getId().equals(customerDTO.getUserId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        Customer result = customerRepository.save(customer);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("customer", customerDTO.getId().toString()))
                .body(customerMapper.customerToCustomerDTO(result));
    }

    /**
     * GET  /customers -> get all the customers.
     */
    @RequestMapping(value = "/customers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CustomerDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        Page<Customer> page = customerRepository.findByUserId(currentUser.get().getId(), PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customers", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(customerMapper::customerToCustomerDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /customers/:id -> get the "id" customer.
     */
    @RequestMapping(value = "/customers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Customer : {}", id);
        Customer customer = customerRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(customer.getUser() == null || !currentUser.get().getId().equals(customer.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        return Optional.ofNullable(customer)
            .map(customerMapper::customerToCustomerDTO)
            .map(customerDTO -> new ResponseEntity<>(
                customerDTO,
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
        Customer customer = customerRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(customer.getUser() == null || !currentUser.get().getId().equals(customer.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        customerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customer", id.toString())).build();
    }

    /**
     * GET  /customerMapByCustomerIds -> get the customer map against the "customerIds".
     */
    @RequestMapping(value = "/customerMapByCustomerIds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Map<Long, Customer>> getCustomerMapByCustomerIds (@RequestParam(value = "customerIds" , required = true) String customerIds) {
        log.debug("REST request by '{}' to get Customer Map by Customer Ids {}", SecurityUtils.getCurrentLogin(), customerIds);
        String[] customerIdsStrArr = customerIds.split(",");
        Map<Long, Customer> customerMap = new HashMap<>();
        List<Long> customerIdList = new ArrayList<>(customerIdsStrArr.length);
        for(String customerIdStr : customerIdsStrArr) {
            try {
                customerIdList.add(Long.parseLong(customerIdStr));
            } catch (Exception e) {
                return ResponseEntity.badRequest().header("Failure", "Invalid customerIds").body(null);
            }
        }
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        List<Customer> customers = customerRepository.findByUserIdAndCustomerIdIn(currentUser.get().getId(), customerIdList);
        if(customers != null && customers.size() > 0 && currentUser.get() != null) {
            for(Customer customer : customers) {
                if(customer != null) {
                    if(customer.getUser() == null || customer.getUser().getId() == null
                        || !currentUser.get().getId().equals(customer.getUser().getId())) {
                        return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
                    }
                    customerMap.put(customer.getId(), customer);
                }
            }
        }
        return Optional.ofNullable(customerMap)
            .map(channelPost -> new ResponseEntity<>(
                channelPost,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
