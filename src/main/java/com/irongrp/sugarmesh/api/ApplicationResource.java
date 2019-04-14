package com.irongrp.sugarmesh.api;

import com.irongrp.sugarmesh.api.model.ApplicationDTO;
import com.irongrp.sugarmesh.api.model.ApplicationMapper;
import com.irongrp.sugarmesh.service.dependency.DependencyService;
import com.irongrp.sugarmesh.service.dependency.model.Application;
import com.irongrp.sugarmesh.service.user.DependencyUserService;
import com.irongrp.sugarmesh.service.user.model.User;
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

    @Autowired
    private DependencyUserService userService;

    @RequestMapping(value = "/nop", method = RequestMethod.GET)
    public String nop() {
        return "OK";
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    public ApplicationDTO updateApplication(@RequestBody ApplicationDTO applicationDTO) throws Exception {
        Application app = ApplicationMapper.map(applicationDTO);
        User user = userService.getUser("app");
        if (user == null) {
            user = new User();
            user.setUsername("app");
        }
        app.setCreatedBy(user);
        return ApplicationMapper.map(dependencyService.updateApplication(app));
    }
}
