package com.irongrp.sugarmesh.service.dependency;

import com.irongrp.sugarmesh.service.dependency.model.Application;
import com.irongrp.sugarmesh.service.user.DependencyUserService;
import com.irongrp.sugarmesh.service.user.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.beans.BeansEndpoint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DependencyServiceTest {

    @Autowired
    private DependencyService dependencyService;

    @Autowired
    private DependencyUserService dependencyUserService;

    @Autowired
    private BeansEndpoint endpoint;

    @Test
    public void createDependencyGraph() throws Exception {
        final User appUser = getAppUser();
        Map<String, Application> applicationMap = dependencyService.createDependencyGraph(appUser,endpoint.beans(),"com.irongrp");
        applicationMap.values().forEach(dependencyService::updateApplication);
    }

    private User getAppUser() {
        User appUser = dependencyUserService.getUser("application");
        if (appUser == null) {
            appUser = dependencyUserService.register("application","");
        }
        return appUser;
    }

}