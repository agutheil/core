package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.CustomerOrder;
import com.mightymerce.core.domain.User;
import com.mightymerce.core.domain.enumeration.OrderStatus;
import com.mightymerce.core.repository.CustomerOrderRepository;
import com.mightymerce.core.repository.UserRepository;
import com.mightymerce.core.security.SecurityUtils;
import com.mightymerce.core.web.rest.util.HeaderUtil;
import com.mightymerce.core.web.rest.util.PaginationUtil;
import com.mightymerce.core.web.rest.dto.CustomerOrderDTO;
import com.mightymerce.core.web.rest.mapper.CustomerOrderMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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
     * GET  /customerOrderByStatus/:orderStatus -> get all the customerOrder by "orderStatus"
     */
    @RequestMapping(value = "/customerOrderByStatus/{orderStatus}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<CustomerOrderDTO> getByOrderStatus(@RequestParam(value = "orderStatus" , required = false) String orderStatus)
        throws URISyntaxException {
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        return customerOrderRepository.findByUserIdAndOrderStatus(currentUser.get().getId(), OrderStatus.valueOf(orderStatus), new Sort(Sort.Direction.ASC, "placedOn")).stream()
            .map(customerOrderMapper::customerOrderToCustomerOrderDTO)
            .collect(Collectors.toCollection(LinkedList::new));
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

    /**
     * GET  /performanceStats -> get performance stats.
     */
    @RequestMapping(value = "/performanceStats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public Map<String, Object> getPerformanceStats(
        @RequestParam(value = "performanceStatOption" , required = true) Long performanceStatOption
        , @RequestParam(value = "startDate" , required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ") DateTime startDate
        , @RequestParam(value = "endDate" , required = false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ") DateTime endDate)
        throws URISyntaxException {

        log.debug("REST request by '{}' to get Performance Stats with option {}, startDate {}, endDate {}"
            , SecurityUtils.getCurrentLogin(), performanceStatOption, startDate, endDate);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());

        Long orderCount = null;
        if(performanceStatOption == 1) {
            orderCount = customerOrderRepository.getOrderCountPastDay(currentUser.get().getId());
        } else if(performanceStatOption == 2) {
            orderCount = customerOrderRepository.getOrderCountPast7Day(currentUser.get().getId());
        } else if(performanceStatOption == 3) {
            orderCount = customerOrderRepository.getOrderCountCurrentMonth(currentUser.get().getId());
        } else if(performanceStatOption == 4 && startDate != null && endDate != null) {
            orderCount = customerOrderRepository.getOrderCountCustom(currentUser.get().getId(), startDate.toDate(), endDate.toDate());
        }
        if(orderCount == null) {
            orderCount = 0l;
        }

        Double averageCart = null;
        if(performanceStatOption == 1) {
            averageCart = customerOrderRepository.getAverageCartPastDay(currentUser.get().getId());
        } else if(performanceStatOption == 2) {
            averageCart = customerOrderRepository.getAverageCartPast7Day(currentUser.get().getId());
        } else if(performanceStatOption == 3) {
            averageCart = customerOrderRepository.getAverageCartCurrentMonth(currentUser.get().getId());
        } else if(performanceStatOption == 4 && startDate != null && endDate != null) {
            averageCart = customerOrderRepository.getAverageCartCustom(currentUser.get().getId(), startDate.toDate(), endDate.toDate());
        }
        if(averageCart == null) {
            averageCart = 0d;
        }

        Double revenue = null;
        if(performanceStatOption == 1) {
            revenue = customerOrderRepository.getRevenuePastDay(currentUser.get().getId());
        } else if(performanceStatOption == 2) {
            revenue = customerOrderRepository.getRevenuePast7Day(currentUser.get().getId());
        } else if(performanceStatOption == 3) {
            revenue = customerOrderRepository.getRevenueCurrentMonth(currentUser.get().getId());
        } else if(performanceStatOption == 4 && startDate != null && endDate != null) {
            revenue = customerOrderRepository.getRevenueCustom(currentUser.get().getId(), startDate.toDate(), endDate.toDate());
        }
        if(revenue == null) {
            revenue = 0d;
        }

        Double salesVolume = null;
        if(performanceStatOption == 1) {
            salesVolume = customerOrderRepository.getSalesVolumePastDay(currentUser.get().getId());
        } else if(performanceStatOption == 2) {
            salesVolume = customerOrderRepository.getSalesVolumePast7Day(currentUser.get().getId());
        } else if(performanceStatOption == 3) {
            salesVolume = customerOrderRepository.getSalesVolumeCurrentMonth(currentUser.get().getId());
        } else if(performanceStatOption == 4 && startDate != null && endDate != null) {
            salesVolume = customerOrderRepository.getSalesVolumeCustom(currentUser.get().getId(), startDate.toDate(), endDate.toDate());
        }
        if(salesVolume == null) {
            salesVolume = 0d;
        }

        Double returnVolume = null;
        if(performanceStatOption == 1) {
            returnVolume = customerOrderRepository.getReturnVolumePastDay(currentUser.get().getId());
        } else if(performanceStatOption == 2) {
            returnVolume = customerOrderRepository.getReturnVolumePast7Day(currentUser.get().getId());
        } else if(performanceStatOption == 3) {
            returnVolume = customerOrderRepository.getReturnVolumeCurrentMonth(currentUser.get().getId());
        } else if(performanceStatOption == 4 && startDate != null && endDate != null) {
            returnVolume = customerOrderRepository.getReturnVolumeCustom(currentUser.get().getId(), startDate.toDate(), endDate.toDate());
        }
        if(returnVolume == null) {
            returnVolume = 0d;
        }

        Map<String, Object> performanceStats = new HashMap<>();
        performanceStats.put("customerCount", 0);
        performanceStats.put("orderCount", orderCount);
        performanceStats.put("averageCart", averageCart);
        performanceStats.put("revenue", revenue);
        performanceStats.put("salesVolume", salesVolume);
        performanceStats.put("returnVolume", returnVolume);
        return performanceStats;
    }

}
