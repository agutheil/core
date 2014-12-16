package com.schubber.web;

import com.schubber.model.Product;
import com.schubber.model.ProductRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by agutheil on 16.12.14.
 */
@RestController
@RequestMapping("/products")
public class ProductController {
    @Resource
    private ProductRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Product> products() {
        return repository.findAll();
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public Product product(@PathVariable long id) {
        return repository.findById(id);
    }
}
