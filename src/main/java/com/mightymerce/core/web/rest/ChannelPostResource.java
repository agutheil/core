package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.Article;
import com.mightymerce.core.domain.ChannelPost;
import com.mightymerce.core.domain.User;
import com.mightymerce.core.domain.enumeration.Channel;
import com.mightymerce.core.domain.enumeration.PublicationStatus;
import com.mightymerce.core.repository.ChannelPostRepository;
import com.mightymerce.core.repository.UserRepository;
import com.mightymerce.core.security.SecurityUtils;
import com.mightymerce.core.service.ChannelPostService;
import com.mightymerce.core.web.rest.util.HeaderUtil;
import com.mightymerce.core.web.rest.util.PaginationUtil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
import java.util.*;

/**
 * REST controller for managing ChannelPost.
 */
@RestController
@RequestMapping("/api")
public class ChannelPostResource {

    private final Logger log = LoggerFactory.getLogger(ChannelPostResource.class);

    @Inject
    private ChannelPostRepository channelPostRepository;

    @Inject
    private ChannelPostService channelPostService;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /channelPosts -> Create a new channelPost.
     */
    @RequestMapping(value = "/channelPosts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity create(@RequestBody ChannelPost channelPost) throws URISyntaxException {
        log.debug("REST request to save ChannelPost : {}", channelPost);
        if (channelPost.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new channelPost cannot already have an ID").body(null);
        }
        log.debug("1 => " + channelPost);
        channelPost.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        log.debug("2 => " + channelPost);
        channelPost = channelPostRepository.save(channelPost);
        log.debug("3 => " + channelPost);
        String merchantChannelError = "Error while processing  request";

        /**
         * TESTING CODE - STARTS HERE
         * Only for testing - should not go in to production
         */
        if(channelPost.getStatus() == null ||
            (!channelPost.getStatus().equals(PublicationStatus.Published)
                && !channelPost.getStatus().equals(PublicationStatus.Pending)
                && !channelPost.getStatus().equals(PublicationStatus.Error))) {
            log.debug("     #####     TRYING TO POST TO FACEBOOK     ######     ");
            /**
             * TESTING CODE - ENDS HERE
             */
            try {
                channelPost.setExternalPostKey(channelPostService.updateStatus(channelPost));
                channelPost.setStatus(PublicationStatus.Published);
            } catch (Exception e) {
                merchantChannelError = "Error while posting to facebook : " + e.toString();
                log.error(merchantChannelError);
                channelPost.setStatus(PublicationStatus.Error);
            }
            /**
             * TESTING CODE - STARTS HERE
             * Only for testing - should not go in to production
             */
        } else {
            log.debug("     #####     SKIPPING POST TO FACEBOOK     ######     ");
        }
        /**
         * TESTING CODE - ENDS HERE
         */
        channelPost.setPublicationDate(DateTime.now(DateTimeZone.UTC));
        log.debug("4 => " + channelPost);
        channelPost = channelPostRepository.save(channelPost);
        log.debug("5 => " + channelPost);
/*
        if (merchantChannelError != null) {
            return ResponseEntity.badRequest().header("Failure", merchantChannelError).body("Error while posting to Facebook. Please try again after some time and if problem persist, contact administrator.");
        }
*/
        HttpHeaders httpHeaders = null;
        if(channelPost.getStatus().equals(PublicationStatus.Published)) {
            httpHeaders = HeaderUtil.createEntityCreationAlert("channelPost", channelPost.getId().toString());
        } else if(channelPost.getStatus().equals(PublicationStatus.Pending)) {
            httpHeaders = HeaderUtil.createWarningAlert("Your post request has been sent. Waiting for confirmation by Facebook within the next 10 minutes.");
        } else if(channelPost.getStatus().equals(PublicationStatus.Error)) {
            httpHeaders = HeaderUtil.createErrorAlert(merchantChannelError);
        }
        return ResponseEntity.created(new URI("/api/channelPosts/" + channelPost.getId()))
            .headers(httpHeaders).body(channelPost);
    }

    /**
     * PUT  /channelPosts -> Updates an existing channelPost.
     */
    @RequestMapping(value = "/channelPosts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChannelPost> update(@RequestBody ChannelPost channelPost) throws URISyntaxException {
        log.debug("REST request to update ChannelPost : {}", channelPost);
        if (channelPost.getId() == null) {
            return create(channelPost);
        }
        channelPost.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        ChannelPost result = channelPostRepository.save(channelPost);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("channelPost", channelPost.getId().toString()))
            .body(result);
    }

    /**
     * GET  /channelPosts -> get all the channelPosts.
     */
    @RequestMapping(value = "/channelPosts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ChannelPost>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                                    @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request by '{}' to getAll ChannelPost with page {} and per_page {}", SecurityUtils.getCurrentLogin(), offset, limit);
        Page<ChannelPost> page = channelPostRepository.findByUserIsCurrentUser(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/channelPosts", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /channelPosts?productId={productId} -> get the channelPost against the "productId".
     */
/*
    @RequestMapping(value = "/channelPosts?productId={productId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChannelPost> getByProductId(@RequestParam(value = "productId" , required = true) Long productId) {
        log.debug("REST request by '{}' to get ChannelPost by Product Id {}", SecurityUtils.getCurrentLogin(), productId);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        List<ChannelPost> channelPosts = channelPostRepository.findByUserIdAndProductId(currentUser.get().getId(), productId);
        if(channelPosts == null || channelPosts.size() <= 0) {
            return ResponseEntity.badRequest().header("Failure", "Resource not found").body(null);
        }
        if(currentUser.get() == null || channelPosts.get(0) == null || channelPosts.get(0).getUser() == null
            || channelPosts.get(0).getUser().getId() == null || !currentUser.get().getId().equals(channelPosts.get(0).getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        return Optional.ofNullable(channelPosts.get(0))
            .map(channelPost -> new ResponseEntity<>(
                channelPost,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
*/

    /**
     * GET  /channelPostsByProductIds -> get the channelPosts against the "productIds".
     */
    @RequestMapping(value = "/channelPostsByProductIds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Map<Long, ChannelPost>> getByProductIds(@RequestParam(value = "productIds" , required = true) String productIds) {
        log.debug("REST request by '{}' to get ChannelPosts by Product Ids {}", SecurityUtils.getCurrentLogin(), productIds);
        String[] productIdsStrArr = productIds.split(",");
        Map<Long, ChannelPost> channelPostMap = new HashMap<>();
        List<Long> productIdList = new ArrayList<>(productIdsStrArr.length);
        for(String productIdStr : productIdsStrArr) {
            try {
                productIdList.add(Long.parseLong(productIdStr));
            } catch (Exception e) {
                return ResponseEntity.badRequest().header("Failure", "Invalid productIds").body(null);
            }
        }
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        List<ChannelPost> channelPosts = channelPostRepository.findByUserIdAndProductIdIn(currentUser.get().getId(), productIdList);
        if(channelPosts != null && channelPosts.size() > 0 && currentUser.get() != null) {
            for(ChannelPost channelPost : channelPosts) {
                if(channelPost != null) {
                    if(channelPost.getUser() == null || channelPost.getUser().getId() == null
                        || !currentUser.get().getId().equals(channelPost.getUser().getId())) {
                        return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
                    }
                    channelPostMap.put(channelPost.getProduct().getId(), channelPost);
                }
            }
        }
        return Optional.ofNullable(channelPostMap)
            .map(channelPost -> new ResponseEntity<>(
                channelPost,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /channelPosts/:id -> get the "id" channelPost.
     */
    @RequestMapping(value = "/channelPosts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChannelPost> get(@PathVariable Long id) {
        log.debug("REST request to get ChannelPost : {}", id);
        return Optional.ofNullable(channelPostRepository.findOne(id))
            .map(channelPost -> new ResponseEntity<>(
                channelPost,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /channelPosts/:id -> delete the "id" channelPost.
     */
    @RequestMapping(value = "/channelPosts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete ChannelPost : {}", id);
        channelPostRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("channelPost", id.toString())).build();
    }
}
