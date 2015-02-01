package com.schubber.web;

import com.schubber.model.AccountRepository;
import com.schubber.model.Product;
import com.schubber.model.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/{userId}/products")
public class ProductRestController {

    private ProductRepository productRepository;
    private AccountRepository accountRepository;

    @Autowired
    ProductRestController(ProductRepository productRepository,
                          AccountRepository accountRepository) {
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(@PathVariable String userId, @RequestBody Product product) {
        validateUser(userId);
        return accountRepository
                .findByUsername(userId)
                .map(account -> {
                    Product result = productRepository.save(
                            new Product(account, product.getTitle(), product.getPrice(),
                                    product.getCurrency(), product.getStock()));
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setLocation(ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri());
                    return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
                }).get();
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    ProductResource readProduct(@PathVariable String userId, @PathVariable Long productId) {
        this.validateUser(userId);
        Product product = this.productRepository.findOne(productId);
        return new ProductResource(product);
    }

    @RequestMapping(method = RequestMethod.GET)
    Resources<ProductResource> readProducts(@PathVariable String userId) {
        this.validateUser(userId);
        List<ProductResource> productList = productRepository.findByAccountUsername(userId)
                .stream()
                .map(ProductResource::new)
                .collect(Collectors.toList());
        return new Resources<ProductResource>(productList);
    }

    private void validateUser(String userId) {
        this.accountRepository.findByUsername(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
    }
}
