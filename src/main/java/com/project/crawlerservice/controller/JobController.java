package com.project.crawlerservice.controller;

import com.project.crawlerservice.job.StarterJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crawler/job")
public class JobController {

    @Autowired
    private StarterJob starterJob;

    @GetMapping(path = {"/run/{name}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void run(@PathVariable String name){
        starterJob.run(name);
    }

}
