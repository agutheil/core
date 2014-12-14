package com.schubber.helloworld;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class Application {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello Schubber!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}