package com.example.shopme.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan({"com.example.shopme.common.model.entity"})
public class ShopMeBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopMeBackEndApplication.class, args);
    }

}
