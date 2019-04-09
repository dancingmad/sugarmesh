package com.irongrp.sugarmesh.api;

import com.irongrp.sugarmesh.service.dependency.DependencyService;
import com.irongrp.sugarmesh.service.dependency.model.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/application")
public class ApplicationResource {

    @Autowired
    private DependencyService dependencyService;

    @RequestMapping(value = "/nop", method = RequestMethod.GET)
    public String nop() {
        return "OK";
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public Application updateApplication(@RequestBody Application application) throws Exception {
        return dependencyService.updateApplication(application);
    }
}
