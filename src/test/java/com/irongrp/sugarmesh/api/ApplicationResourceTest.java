package com.irongrp.sugarmesh.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irongrp.sugarmesh.service.dependency.DependencyService;
import com.irongrp.sugarmesh.service.dependency.model.Application;
import com.irongrp.sugarmesh.service.user.DependencyUserService;
import com.irongrp.sugarmesh.service.user.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.beans.BeansEndpoint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ApplicationResourceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private DependencyUserService dependencyUserService;

    @Autowired
    private DependencyService dependencyService;

    @Autowired
    private BeansEndpoint endpoint;

    private MockMvc mockMvc;
    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testNop() throws Exception {
        String result = this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/application/nop"))
                .andReturn().getResponse()
                .getContentAsString();
        Assert.assertEquals("OK",result);
    }

    @Test
    public void updateBeans() throws Exception {
        // this here is the code that will reside in the application we want to analyse
        User appUser = getAppUser();
        Map<String,Application> applicationMap =
                dependencyService.createDependencyGraph(appUser,endpoint.beans(),"com.irongrp");
        assertEquals(1,applicationMap.size());
        applicationMap.values()
                .stream()
                .map(this::writeValueAsString)
                .forEach(app -> {
                    try {
                        String result = this.mockMvc
                                .perform(MockMvcRequestBuilders.put("/api/application/")
                                        .content(app).contentType("application/json"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString();
                        Assert.assertFalse(result.isEmpty());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private String writeValueAsString(Application app) {
        try {
            return objectMapper.writeValueAsString(app);
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }

    private User getAppUser() {
        User appUser = dependencyUserService.getUser("application");
        if (appUser == null) {
            appUser = dependencyUserService.register("application","");
        }
        return appUser;
    }

}