package com.valderson.shoppingcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class ShoppingCartApplication {

    private ShoppingCartApplication() {
        // Utility class constructor
    }

    public static void main(final String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }

}
