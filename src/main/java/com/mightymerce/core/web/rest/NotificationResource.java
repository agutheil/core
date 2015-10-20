package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.Notification;
import com.mightymerce.core.domain.User;
import com.mightymerce.core.repository.NotificationRepository;
import com.mightymerce.core.repository.UserRepository;
import com.mightymerce.core.security.SecurityUtils;
import com.mightymerce.core.web.rest.util.HeaderUtil;
import com.mightymerce.core.web.rest.dto.NotificationDTO;
import com.mightymerce.core.web.rest.mapper.NotificationMapper;
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
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private NotificationMapper notificationMapper;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /notifications -> Create a new notification.
     */
    @RequestMapping(value = "/notifications",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationDTO> create(@Valid @RequestBody NotificationDTO notificationDTO) throws URISyntaxException {
        log.debug("REST request to save Notification : {}", notificationDTO);
        if (notificationDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new notification cannot already have an ID").body(null);
        }
        Notification notification = notificationMapper.notificationDTOToNotification(notificationDTO);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        notification.setUser(currentUser.get());
        Notification result = notificationRepository.save(notification);
        return ResponseEntity.created(new URI("/api/notifications/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("notification", result.getId().toString()))
                .body(notificationMapper.notificationToNotificationDTO(result));
    }

    /**
     * PUT  /notifications -> Updates an existing notification.
     */
    @RequestMapping(value = "/notifications",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationDTO> update(@Valid @RequestBody NotificationDTO notificationDTO) throws URISyntaxException {
        log.debug("REST request to update Notification : {}", notificationDTO);
        if (notificationDTO.getId() == null) {
            return create(notificationDTO);
        }
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(!currentUser.get().getId().equals(notificationDTO.getUserId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        Notification notification = notificationMapper.notificationDTOToNotification(notificationDTO);
        Notification result = notificationRepository.save(notification);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("notification", notificationDTO.getId().toString()))
                .body(notificationMapper.notificationToNotificationDTO(result));
    }

    /**
     * GET  /notifications -> get all the notifications.
     */
    @RequestMapping(value = "/notifications",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<NotificationDTO> getAll() {
        log.debug("REST request to get all Notifications");
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        return notificationRepository.findByUserId(currentUser.get().getId()).stream()
            .map(notification -> notificationMapper.notificationToNotificationDTO(notification))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * GET  /notifications/:id -> get the "id" notification.
     */
    @RequestMapping(value = "/notifications/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Notification : {}", id);
        Notification notification = notificationRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(notification.getUser() == null || !currentUser.get().getId().equals(notification.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        return Optional.ofNullable(notification)
            .map(notificationMapper::notificationToNotificationDTO)
            .map(notificationDTO -> new ResponseEntity<>(
                notificationDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notifications/:id -> delete the "id" notification.
     */
    @RequestMapping(value = "/notifications/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Notification : {}", id);
        Notification notification = notificationRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(notification.getUser() == null || !currentUser.get().getId().equals(notification.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        notificationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("notification", id.toString())).build();
    }
}
