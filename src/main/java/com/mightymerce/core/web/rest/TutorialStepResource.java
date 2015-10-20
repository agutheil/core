package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.TutorialStep;
import com.mightymerce.core.domain.User;
import com.mightymerce.core.repository.TutorialStepRepository;
import com.mightymerce.core.repository.UserRepository;
import com.mightymerce.core.security.SecurityUtils;
import com.mightymerce.core.web.rest.util.HeaderUtil;
import com.mightymerce.core.web.rest.dto.TutorialStepDTO;
import com.mightymerce.core.web.rest.mapper.TutorialStepMapper;
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
 * REST controller for managing TutorialStep.
 */
@RestController
@RequestMapping("/api")
public class TutorialStepResource {

    private final Logger log = LoggerFactory.getLogger(TutorialStepResource.class);

    @Inject
    private TutorialStepRepository tutorialStepRepository;

    @Inject
    private TutorialStepMapper tutorialStepMapper;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /tutorialSteps -> Create a new tutorialStep.
     */
    @RequestMapping(value = "/tutorialSteps",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TutorialStepDTO> create(@Valid @RequestBody TutorialStepDTO tutorialStepDTO) throws URISyntaxException {
        log.debug("REST request to save TutorialStep : {}", tutorialStepDTO);
        if (tutorialStepDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new tutorialStep cannot already have an ID").body(null);
        }
        TutorialStep tutorialStep = tutorialStepMapper.tutorialStepDTOToTutorialStep(tutorialStepDTO);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        tutorialStep.setUser(currentUser.get());
        TutorialStep result = tutorialStepRepository.save(tutorialStep);
        return ResponseEntity.created(new URI("/api/tutorialSteps/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("tutorialStep", result.getId().toString()))
                .body(tutorialStepMapper.tutorialStepToTutorialStepDTO(result));
    }

    /**
     * PUT  /tutorialSteps -> Updates an existing tutorialStep.
     */
    @RequestMapping(value = "/tutorialSteps",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TutorialStepDTO> update(@Valid @RequestBody TutorialStepDTO tutorialStepDTO) throws URISyntaxException {
        log.debug("REST request to update TutorialStep : {}", tutorialStepDTO);
        if (tutorialStepDTO.getId() == null) {
            return create(tutorialStepDTO);
        }
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(!currentUser.get().getId().equals(tutorialStepDTO.getUserId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        TutorialStep tutorialStep = tutorialStepMapper.tutorialStepDTOToTutorialStep(tutorialStepDTO);
        TutorialStep result = tutorialStepRepository.save(tutorialStep);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("tutorialStep", tutorialStepDTO.getId().toString()))
                .body(tutorialStepMapper.tutorialStepToTutorialStepDTO(result));
    }

    /**
     * GET  /tutorialSteps -> get all the tutorialSteps.
     */
    @RequestMapping(value = "/tutorialSteps",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<TutorialStepDTO> getAll() {
        log.debug("REST request to get all TutorialSteps");
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        return tutorialStepRepository.findByUserId(currentUser.get().getId()).stream()
            .map(tutorialStep -> tutorialStepMapper.tutorialStepToTutorialStepDTO(tutorialStep))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * GET  /tutorialSteps/:id -> get the "id" tutorialStep.
     */
    @RequestMapping(value = "/tutorialSteps/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TutorialStepDTO> get(@PathVariable Long id) {
        log.debug("REST request to get TutorialStep : {}", id);
        TutorialStep tutorialStep = tutorialStepRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(tutorialStep.getUser() == null || !currentUser.get().getId().equals(tutorialStep.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        return Optional.ofNullable(tutorialStep)
            .map(tutorialStepMapper::tutorialStepToTutorialStepDTO)
            .map(tutorialStepDTO -> new ResponseEntity<>(
                tutorialStepDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tutorialSteps/:id -> delete the "id" tutorialStep.
     */
    @RequestMapping(value = "/tutorialSteps/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete TutorialStep : {}", id);
        TutorialStep tutorialStep = tutorialStepRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(tutorialStep.getUser() == null || !currentUser.get().getId().equals(tutorialStep.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        tutorialStepRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tutorialStep", id.toString())).build();
    }
}
