package com.senhorcafe.urlshortner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication()
@ConfigurationPropertiesScan
public class UrlShortnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShortnerApplication.class, args);
    }

}
