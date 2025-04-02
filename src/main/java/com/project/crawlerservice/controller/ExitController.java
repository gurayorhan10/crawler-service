package com.project.crawlerservice.controller;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application")
public class ExitController {

    @Autowired
    private EurekaClient client;

    @Autowired
    private ApplicationContext context;

    @GetMapping("/close")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void close(){
        client.shutdown();
    }

    @GetMapping("/exit")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void exit(){
        SpringApplication.exit(context);
    }

}