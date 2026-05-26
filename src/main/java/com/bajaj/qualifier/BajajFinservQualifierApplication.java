package com.bajaj.qualifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BajajFinservQualifierApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BajajFinservQualifierApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}

