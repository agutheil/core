package com.schubber.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by agutheil on 16.12.14.
 */
@RestController
@RequestMapping("/")
public class RootController {
    @RequestMapping(method = RequestMethod.GET)
    public String hello(){
        return "Hello Schubber!";
    }
}
