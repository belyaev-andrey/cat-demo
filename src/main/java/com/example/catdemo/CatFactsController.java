package com.example.catdemo;

import org.springframework.context.annotation.Configuration;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.registry.ImportHttpServices;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@EnableResilientMethods
@ImportHttpServices(CatFactsClient.class)
@Configuration
class CatFactsConfig {
}

@Controller
@ResponseBody
class CatFactsController {

    private final CatFactsClient catFactsClient;

    private final AtomicInteger counter = new AtomicInteger(0);

    CatFactsController(CatFactsClient catFactsClient) {
        this.catFactsClient = catFactsClient;
    }

    @ConcurrencyLimit(10)
    @Retryable(maxRetries = 4, includes = IllegalStateException.class)
    @GetMapping("/cats")
    CatFacts facts() {
        if (counter.incrementAndGet() < 4 ) {
            IO.println("ouch!");
            throw new IllegalStateException("ouch!");
        }
        IO.println("got 'em!");
        return catFactsClient.facts();
    }

}


interface CatFactsClient {
    @GetExchange(url = "https://www.catfacts.net/api")
    CatFacts facts();
}

record CatFacts(Collection<CatFact> facts){}

record CatFact(String fact){}
