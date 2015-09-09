package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.Article;
import com.mightymerce.core.domain.FlatSocialOrder;
import com.mightymerce.core.domain.SocialOrder;
import com.mightymerce.core.domain.User;
import com.mightymerce.core.domain.enumeration.OrderStatus;
import com.mightymerce.core.domain.enumeration.PaymentStatus;
import com.mightymerce.core.repository.ArticleRepository;
import com.mightymerce.core.repository.SocialOrderRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SocialOrder.
 */
@RestController
@RequestMapping("/api")
public class SocialOrderResource {

    private final Logger log = LoggerFactory.getLogger(SocialOrderResource.class);

    @Inject
    private SocialOrderRepository socialOrderRepository;
    
    @Inject
    private ArticleRepository articleRepository;
    
    @Inject
    private UserRepository userRepository;

    /**
     * POST  /socialOrders -> Create a new socialOrder.
     */
    @RequestMapping(value = "/socialOrders",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SocialOrder> create(@RequestBody SocialOrder socialOrder) throws URISyntaxException {
        log.debug("REST request to save SocialOrder : {}", socialOrder);
        if (socialOrder.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new socialOrder cannot already have an ID").body(null);
        }
        socialOrder.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        SocialOrder result = socialOrderRepository.save(socialOrder);
        return ResponseEntity.created(new URI("/api/socialOrders/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("socialOrder", result.getId().toString()))
                .body(result);
    }
    
    /**
     * POST  /flatSocialOrders -> Create a new socialOrder via a flattened Model.
     */
    @RequestMapping(value = "/flatSocialOrders",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlatSocialOrder> createflat(@RequestBody FlatSocialOrder flatSocialOrder) throws URISyntaxException {
        log.debug("REST request to save SocialOrder via a FlatSocialOrder: {}", flatSocialOrder);
        if (flatSocialOrder.getArticle() == null) {
            return ResponseEntity.badRequest().header("Failure", "A new socialOrder needs an article ID").body(null);
        }
        SocialOrder input = new SocialOrder();
        Article article = articleRepository.findOne(flatSocialOrder.getArticle());
        log.info(article.toString());
        input.setArticle(article);
        input.setPaymentStatus(PaymentStatus.valueOf(flatSocialOrder.getPaymentStatus().toLowerCase()));
        input.setOrderStatus(OrderStatus.created);
        input.setPayerId(flatSocialOrder.getPayerId());
        input.setTransactionId(flatSocialOrder.getTransactionId());
        input.setTotalAmount(flatSocialOrder.getAmount());
        User user = userRepository.findOne(article.getUser().getId());
        log.info(user.toString());
        input.setUser(user);
        SocialOrder result = socialOrderRepository.save(input);
        return ResponseEntity.created(new URI("/api/socialOrders/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("socialOrder", result.getId().toString()))
                .body(flatSocialOrder);
    }

    /**
     * PUT  /socialOrders -> Updates an existing socialOrder.
     */
    @RequestMapping(value = "/socialOrders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SocialOrder> update(@RequestBody SocialOrder socialOrder) throws URISyntaxException {
        log.debug("REST request to update SocialOrder : {}", socialOrder);
        if (socialOrder.getId() == null) {
            return create(socialOrder);
        }
        socialOrder.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        SocialOrder result = socialOrderRepository.save(socialOrder);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("socialOrder", socialOrder.getId().toString()))
                .body(result);
    }

    /**
     * GET  /socialOrders -> get all the socialOrders.
     */
    @RequestMapping(value = "/socialOrders",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SocialOrder>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<SocialOrder> page = socialOrderRepository.findByUserIsCurrentUser(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/socialOrders", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /socialOrders/:id -> get the "id" socialOrder.
     */
    @RequestMapping(value = "/socialOrders/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SocialOrder> get(@PathVariable Long id) {
        log.debug("REST request to get SocialOrder : {}", id);
        return Optional.ofNullable(socialOrderRepository.findOne(id))
            .map(socialOrder -> new ResponseEntity<>(
                socialOrder,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /socialOrders/:id -> delete the "id" socialOrder.
     */
    @RequestMapping(value = "/socialOrders/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete SocialOrder : {}", id);
        socialOrderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("socialOrder", id.toString())).build();
    }
}
