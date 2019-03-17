package com.irongrp.sugarmesh.api;

import com.irongrp.sugarmesh.service.dependency.DependencyService;
import com.irongrp.sugarmesh.service.dependency.model.Application;
import com.irongrp.sugarmesh.service.exception.UnauthorizedException;
import com.irongrp.sugarmesh.service.user.UserService;
import com.irongrp.sugarmesh.service.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/dependencies")
public class DependencyGraphResource {

    @Autowired
    private UserService userService;

    @Autowired
    private DependencyService dependencyService;

    @RequestMapping(path = "/", method = RequestMethod.POST, consumes = "application/json")
    public DependencyGraph createDependencyGraph(@RequestBody DependencyGraph dependencyGraph,
                                                         HttpServletRequest request) {
        User loggedInUser = userService.getLoggedInUser(request);
        return dependencyService.createDependencyGraph(loggedInUser, dependencyGraph);
    }



}
