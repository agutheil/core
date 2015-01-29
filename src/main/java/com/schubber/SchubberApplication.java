package com.schubber;

import com.schubber.model.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class SchubberApplication {

    @Bean
    CommandLineRunner init(AccountRepository accountRepository, ProductRepository productRepository) {
        return (evt) -> Arrays.asList("agutheil,mwagner,tgrupp,ageisler".split(",")).forEach(
                a -> {
                    Account account = accountRepository.save(new Account(a, "password"));
                    productRepository.save(new Product(account,"Mein Produkt", BigDecimal.valueOf(12.99), Currency.EUR, 100L));
                    productRepository.save(new Product(account,"Mein anderes Produkt", BigDecimal.valueOf(7.99), Currency.EUR, 200L));
                }
        );
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SchubberApplication.class, args);
    }
}