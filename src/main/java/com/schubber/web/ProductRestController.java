package com.schubber.web;

import com.schubber.model.Account;
import com.schubber.model.AccountRepository;
import com.schubber.model.Product;
import com.schubber.model.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/{userId}/products")
public class ProductRestController {

    private ProductRepository productRepository;
    private AccountRepository accountRepository;

    @Autowired
    ProductRestController(ProductRepository bookmarkRepository,
                          AccountRepository accountRepository) {
        this.productRepository   = bookmarkRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(@PathVariable String userId, @RequestBody Product product) {
        validateUser(userId);
        return accountRepository
                .findByUsername(userId)
                .map(account -> {
                            Product result = productRepository.save(
                                    new Product(account, product.getTitle(),product.getPrice(),
                                            product.getCurrency(),product.getStock()));
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.setLocation(ServletUriComponentsBuilder
                                    .fromCurrentRequest().path("/{id}")
                                    .buildAndExpand(result.getId()).toUri());
                            return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
                        }).get();
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    Product readBookmark(@PathVariable String userId, @PathVariable Long productId) {
        this.validateUser(userId);
        return this.productRepository.findOne(productId); //hier muss noch genauer gearbeitet werden
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Product> readBookmarks(@PathVariable String userId) {
        this.validateUser(userId);
        return this.productRepository.findByAccountUsername(userId);
    }

    private void validateUser(String userId) {
        this.accountRepository.findByUsername(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
    }
}
