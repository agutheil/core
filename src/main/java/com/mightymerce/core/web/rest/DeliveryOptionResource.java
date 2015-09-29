package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.DeliveryOption;
import com.mightymerce.core.repository.DeliveryOptionRepository;
import com.mightymerce.core.web.rest.util.HeaderUtil;
import com.mightymerce.core.web.rest.dto.DeliveryOptionDTO;
import com.mightymerce.core.web.rest.mapper.DeliveryOptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing DeliveryOption.
 */
@RestController
@RequestMapping("/api")
public class DeliveryOptionResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryOptionResource.class);

    @Inject
    private DeliveryOptionRepository deliveryOptionRepository;

    @Inject
    private DeliveryOptionMapper deliveryOptionMapper;

    /**
     * POST  /deliveryOptions -> Create a new deliveryOption.
     */
    @RequestMapping(value = "/deliveryOptions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeliveryOptionDTO> create(@Valid @RequestBody DeliveryOptionDTO deliveryOptionDTO) throws URISyntaxException {
        log.debug("REST request to save DeliveryOption : {}", deliveryOptionDTO);
        if (deliveryOptionDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new deliveryOption cannot already have an ID").body(null);
        }
        DeliveryOption deliveryOption = deliveryOptionMapper.deliveryOptionDTOToDeliveryOption(deliveryOptionDTO);
        DeliveryOption result = deliveryOptionRepository.save(deliveryOption);
        return ResponseEntity.created(new URI("/api/deliveryOptions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("deliveryOption", result.getId().toString()))
                .body(deliveryOptionMapper.deliveryOptionToDeliveryOptionDTO(result));
    }

    /**
     * PUT  /deliveryOptions -> Updates an existing deliveryOption.
     */
    @RequestMapping(value = "/deliveryOptions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeliveryOptionDTO> update(@Valid @RequestBody DeliveryOptionDTO deliveryOptionDTO) throws URISyntaxException {
        log.debug("REST request to update DeliveryOption : {}", deliveryOptionDTO);
        if (deliveryOptionDTO.getId() == null) {
            return create(deliveryOptionDTO);
        }
        DeliveryOption deliveryOption = deliveryOptionMapper.deliveryOptionDTOToDeliveryOption(deliveryOptionDTO);
        DeliveryOption result = deliveryOptionRepository.save(deliveryOption);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("deliveryOption", deliveryOptionDTO.getId().toString()))
                .body(deliveryOptionMapper.deliveryOptionToDeliveryOptionDTO(result));
    }

    /**
     * GET  /deliveryOptions -> get all the deliveryOptions.
     */
    @RequestMapping(value = "/deliveryOptions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<DeliveryOptionDTO> getAll() {
        log.debug("REST request to get all DeliveryOptions");
        return deliveryOptionRepository.findAll().stream()
            .map(deliveryOption -> deliveryOptionMapper.deliveryOptionToDeliveryOptionDTO(deliveryOption))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * GET  /deliveryOptions/:id -> get the "id" deliveryOption.
     */
    @RequestMapping(value = "/deliveryOptions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeliveryOptionDTO> get(@PathVariable Long id) {
        log.debug("REST request to get DeliveryOption : {}", id);
        return Optional.ofNullable(deliveryOptionRepository.findOne(id))
            .map(deliveryOptionMapper::deliveryOptionToDeliveryOptionDTO)
            .map(deliveryOptionDTO -> new ResponseEntity<>(
                deliveryOptionDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /deliveryOptions/:id -> delete the "id" deliveryOption.
     */
    @RequestMapping(value = "/deliveryOptions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryOption : {}", id);
        deliveryOptionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("deliveryOption", id.toString())).build();
    }
}
