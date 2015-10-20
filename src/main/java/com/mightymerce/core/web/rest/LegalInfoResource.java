package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.LegalInfo;
import com.mightymerce.core.domain.User;
import com.mightymerce.core.repository.LegalInfoRepository;
import com.mightymerce.core.repository.UserRepository;
import com.mightymerce.core.security.SecurityUtils;
import com.mightymerce.core.web.rest.util.HeaderUtil;
import com.mightymerce.core.web.rest.dto.LegalInfoDTO;
import com.mightymerce.core.web.rest.mapper.LegalInfoMapper;
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
 * REST controller for managing LegalInfo.
 */
@RestController
@RequestMapping("/api")
public class LegalInfoResource {

    private final Logger log = LoggerFactory.getLogger(LegalInfoResource.class);

    @Inject
    private LegalInfoRepository legalInfoRepository;

    @Inject
    private LegalInfoMapper legalInfoMapper;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /legalInfos -> Create a new legalInfo.
     */
    @RequestMapping(value = "/legalInfos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LegalInfoDTO> create(@Valid @RequestBody LegalInfoDTO legalInfoDTO) throws URISyntaxException {
        log.debug("REST request to save LegalInfo : {}", legalInfoDTO);
        if (legalInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new legalInfo cannot already have an ID").body(null);
        }
        LegalInfo legalInfo = legalInfoMapper.legalInfoDTOToLegalInfo(legalInfoDTO);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        legalInfo.setUser(currentUser.get());
        LegalInfo result = legalInfoRepository.save(legalInfo);
        return ResponseEntity.created(new URI("/api/legalInfos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("legalInfo", result.getId().toString()))
                .body(legalInfoMapper.legalInfoToLegalInfoDTO(result));
    }

    /**
     * PUT  /legalInfos -> Updates an existing legalInfo.
     */
    @RequestMapping(value = "/legalInfos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LegalInfoDTO> update(@Valid @RequestBody LegalInfoDTO legalInfoDTO) throws URISyntaxException {
        log.debug("REST request to update LegalInfo : {}", legalInfoDTO);
        if (legalInfoDTO.getId() == null) {
            return create(legalInfoDTO);
        }
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(!currentUser.get().getId().equals(legalInfoDTO.getUserId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        LegalInfo legalInfo = legalInfoMapper.legalInfoDTOToLegalInfo(legalInfoDTO);
        LegalInfo result = legalInfoRepository.save(legalInfo);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("legalInfo", legalInfoDTO.getId().toString()))
                .body(legalInfoMapper.legalInfoToLegalInfoDTO(result));
    }

    /**
     * GET  /legalInfos -> get all the legalInfos.
     */
    @RequestMapping(value = "/legalInfos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<LegalInfoDTO> getAll() {
        log.debug("REST request to get all LegalInfos");
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        return legalInfoRepository.findByUserId(currentUser.get().getId()).stream()
            .map(legalInfo -> legalInfoMapper.legalInfoToLegalInfoDTO(legalInfo))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * GET  /legalInfos/:id -> get the "id" legalInfo.
     */
    @RequestMapping(value = "/legalInfos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LegalInfoDTO> get(@PathVariable Long id) {
        log.debug("REST request to get LegalInfo : {}", id);
        LegalInfo legalInfo = legalInfoRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(legalInfo.getUser() == null || !currentUser.get().getId().equals(legalInfo.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        return Optional.ofNullable(legalInfo)
            .map(legalInfoMapper::legalInfoToLegalInfoDTO)
            .map(legalInfoDTO -> new ResponseEntity<>(
                legalInfoDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /legalInfos/:id -> delete the "id" legalInfo.
     */
    @RequestMapping(value = "/legalInfos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete LegalInfo : {}", id);
        LegalInfo legalInfo = legalInfoRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(legalInfo.getUser() == null || !currentUser.get().getId().equals(legalInfo.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        legalInfoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("legalInfo", id.toString())).build();
    }
}
