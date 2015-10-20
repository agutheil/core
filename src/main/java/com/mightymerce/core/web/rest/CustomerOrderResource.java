package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.CustomerOrder;
import com.mightymerce.core.domain.User;
import com.mightymerce.core.repository.CustomerOrderRepository;
import com.mightymerce.core.repository.UserRepository;
import com.mightymerce.core.security.SecurityUtils;
import com.mightymerce.core.web.rest.util.HeaderUtil;
import com.mightymerce.core.web.rest.util.PaginationUtil;
import com.mightymerce.core.web.rest.dto.CustomerOrderDTO;
import com.mightymerce.core.web.rest.mapper.CustomerOrderMapper;
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
 * REST controller for managing CustomerOrder.
 */
@RestController
@RequestMapping("/api")
public class CustomerOrderResource {

    private final Logger log = LoggerFactory.getLogger(CustomerOrderResource.class);

    @Inject
    private CustomerOrderRepository customerOrderRepository;

    @Inject
    private CustomerOrderMapper customerOrderMapper;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /customerOrders -> Create a new customerOrder.
     */
    @RequestMapping(value = "/customerOrders",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerOrderDTO> create(@Valid @RequestBody CustomerOrderDTO customerOrderDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerOrder : {}", customerOrderDTO);
        if (customerOrderDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new customerOrder cannot already have an ID").body(null);
        }
        CustomerOrder customerOrder = customerOrderMapper.customerOrderDTOToCustomerOrder(customerOrderDTO);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        customerOrder.setUser(currentUser.get());
        CustomerOrder result = customerOrderRepository.save(customerOrder);
        return ResponseEntity.created(new URI("/api/customerOrders/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("customerOrder", result.getId().toString()))
                .body(customerOrderMapper.customerOrderToCustomerOrderDTO(result));
    }

    /**
     * PUT  /customerOrders -> Updates an existing customerOrder.
     */
    @RequestMapping(value = "/customerOrders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerOrderDTO> update(@Valid @RequestBody CustomerOrderDTO customerOrderDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerOrder : {}", customerOrderDTO);
        if (customerOrderDTO.getId() == null) {
            return create(customerOrderDTO);
        }
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(!currentUser.get().getId().equals(customerOrderDTO.getUserId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        CustomerOrder customerOrder = customerOrderMapper.customerOrderDTOToCustomerOrder(customerOrderDTO);
        CustomerOrder result = customerOrderRepository.save(customerOrder);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("customerOrder", customerOrderDTO.getId().toString()))
                .body(customerOrderMapper.customerOrderToCustomerOrderDTO(result));
    }

    /**
     * GET  /customerOrders -> get all the customerOrders.
     */
    @RequestMapping(value = "/customerOrders",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CustomerOrderDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        Page<CustomerOrder> page = customerOrderRepository.findByUserId(currentUser.get().getId(), PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customerOrders", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(customerOrderMapper::customerOrderToCustomerOrderDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /customerOrders/:id -> get the "id" customerOrder.
     */
    @RequestMapping(value = "/customerOrders/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerOrderDTO> get(@PathVariable Long id) {
        log.debug("REST request to get CustomerOrder : {}", id);
        CustomerOrder customerOrder = customerOrderRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(customerOrder.getUser() == null || !currentUser.get().getId().equals(customerOrder.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        return Optional.ofNullable(customerOrder)
            .map(customerOrderMapper::customerOrderToCustomerOrderDTO)
            .map(customerOrderDTO -> new ResponseEntity<>(
                customerOrderDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customerOrders/:id -> delete the "id" customerOrder.
     */
    @RequestMapping(value = "/customerOrders/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete CustomerOrder : {}", id);
        CustomerOrder customerOrder = customerOrderRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(customerOrder.getUser() == null || !currentUser.get().getId().equals(customerOrder.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        customerOrderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customerOrder", id.toString())).build();
    }

}
