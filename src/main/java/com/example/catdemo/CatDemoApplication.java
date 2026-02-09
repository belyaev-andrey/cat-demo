package com.example.catdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collection;

@SpringBootApplication
public class CatDemoApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CatDemoApplication.class);
    private final CatFactsClient catFactsClient;

    public CatDemoApplication(CatFactsClient catFactsClient) {
        this.catFactsClient = catFactsClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(CatDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        catFactsClient.facts().facts().forEach(f -> log.info(f.fact()));
    }
}

record CatFact(String fact){}

record CatFacts(Collection<CatFact> facts){}

