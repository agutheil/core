package com.mightymerce.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mightymerce.core.domain.Product;
import com.mightymerce.core.domain.User;
import com.mightymerce.core.repository.ChannelPostRepository;
import com.mightymerce.core.repository.ProductRepository;
import com.mightymerce.core.repository.UserRepository;
import com.mightymerce.core.security.SecurityUtils;
import com.mightymerce.core.web.rest.util.HeaderUtil;
import com.mightymerce.core.web.rest.util.PaginationUtil;
import com.mightymerce.core.web.rest.dto.ProductDTO;
import com.mightymerce.core.web.rest.mapper.ProductMapper;
import org.apache.commons.lang.StringUtils;
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
 * REST controller for managing Product.
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductMapper productMapper;

    @Inject
    private UserRepository userRepository;

    @Inject
    private ChannelPostRepository channelPostRepository;

    /**
     * POST  /products -> Create a new product.
     */
    @RequestMapping(value = "/products",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request by '{}' to save Product : {}", SecurityUtils.getCurrentLogin(), productDTO);
        if (productDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new product cannot already have an ID").body(null);
        }
        Product product = productMapper.productDTOToProduct(productDTO);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        product.setUser(currentUser.get());
        Product result = truncateProductId(productRepository.save(formatProductId(product, currentUser)));
        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("product", result.getId().toString()))
                .body(productMapper.productToProductDTO(result));
    }

    private Product formatProductId (Product product, Optional<User> currentUser) {
        product.setProductId("M" + StringUtils.leftPad("" + currentUser.get().getId(), 19, '0') + "-" + product.getProductId());
        return product;
    }

    private Product truncateProductId (Product product) {
        if(product != null && product.getProductId() != null) {
            String prodId = product.getProductId();
            int sepIndex = prodId.indexOf("-");
            if(sepIndex >= 0)
                product.setProductId(prodId.substring(sepIndex + 1));
        }
        return product;
    }

    /**
     * PUT  /products -> Updates an existing product.
     */
    @RequestMapping(value = "/products",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductDTO> update(@Valid @RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request by '{}' to update Product : {}", SecurityUtils.getCurrentLogin(), productDTO);
        if (productDTO.getId() == null) {
            return create(productDTO);
        }
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(!currentUser.get().getId().equals(productDTO.getUserId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        Product result = truncateProductId(productRepository.save(formatProductId(productMapper.productDTOToProduct(productDTO), currentUser)));
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("product", productDTO.getId().toString()))
                .body(productMapper.productToProductDTO(result));
    }

    /**
     * GET  /products -> get all the products.
     */
    @RequestMapping(value = "/products",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ProductDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request by '{}' to getAll Products with page = {} and per_page = {}", SecurityUtils.getCurrentLogin(), offset, limit);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        Page<Product> page = productRepository.findByUserId(currentUser.get().getId(), PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/products", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(productMapper::productToProductDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /productsWithChannelPosts -> get all the products.
     */
/*
    @RequestMapping(value = "/productsWithChannelPosts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ProductChannelPostDTO>> getAllWithChannelPost(@RequestParam(value = "page" , required = false) Integer offset,
                                                   @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request by '{}' to getAll Products with ChannelPost with page = {} and per_page = {}", SecurityUtils.getCurrentLogin(), offset, limit);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        Page<Product> page = productRepository.findByUserId(currentUser.get().getId(), PaginationUtil.generatePageRequest(offset, limit));
        Iterator<Product> productIterator = page.iterator();
        Map<Long, ProductChannelPostDTO> productChannelPostDTOMap = new HashMap<>();
        Product product;
        List<Long> productIdList = new ArrayList<>();
        while(productIterator.hasNext()) {
            product = productIterator.next();
            productIdList.add(product.getId());
            productChannelPostDTOMap.put(product.getId(), new ProductChannelPostDTO(productMapper.productToProductDTO(product)));
        }

        List<ChannelPost> channelPosts = channelPostRepository.findByUserIdAndProductIdIn(currentUser.get().getId(), productIdList);
        if(channelPosts != null && channelPosts.size() > 0 && currentUser.get() != null) {
            for(ChannelPost channelPost : channelPosts) {
                if(channelPost != null && channelPost.getProduct() != null && channelPost.getProduct().getId() != null
                    && productChannelPostDTOMap.containsKey(channelPost.getProduct().getId())) {
                    productChannelPostDTOMap.get(channelPost.getProduct().getId()).setChannelPost(channelPost);
                }
            }
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/products", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(productMapper::productToProductDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }
*/

    /**
     * GET  /products/:id -> get the "id" product.
     */
    @RequestMapping(value = "/products/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductDTO> get(@PathVariable Long id) {
        log.debug("REST request by '{}' to get Product : {}", SecurityUtils.getCurrentLogin(), id);
        Product product = productRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(product.getUser() == null || !currentUser.get().getId().equals(product.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        return Optional.ofNullable(truncateProductId(product))
            .map(productMapper::productToProductDTO)
            .map(productDTO -> new ResponseEntity<>(
                productDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /products/:id -> delete the "id" product.
     */
    @RequestMapping(value = "/products/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request by '{}' to delete Product : {}", SecurityUtils.getCurrentLogin(), id);
        Product product = productRepository.findOne(id);
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(product.getUser() == null || !currentUser.get().getId().equals(product.getUser().getId())) {
            return ResponseEntity.badRequest().header("Failure", "Permission Denied").body(null);
        }
        productRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("product", id.toString())).build();
    }
}
