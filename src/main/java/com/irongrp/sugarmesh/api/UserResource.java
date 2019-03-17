package com.irongrp.sugarmesh.api;


import com.irongrp.sugarmesh.service.exception.UnauthorizedException;
import com.irongrp.sugarmesh.service.user.UserService;
import com.irongrp.sugarmesh.service.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserResource {

    @Autowired
    private UserService userService;


    @RequestMapping(path = "/{username}/authentication", method = RequestMethod.GET)
    public User authenticate(@PathVariable String username, @RequestParam String password, HttpServletRequest request) {
        User user = userService.authenticate(username, password);
        if (user == null) {
            throw new UnauthorizedException("Invalid Credentials");
        }
        userService.setUserForRequest(request, username);
        return user;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public User authenticate(HttpServletRequest request) {
        User user = userService.getLoggedInUser(request);
        userService.removeUserForRequest(request);
        return user;
    }

    @RequestMapping(path = "/current", method = RequestMethod.GET)
    public User getLoggedInUser(HttpServletRequest request) {
        return userService.getLoggedInUser(request);
    }

    @RequestMapping(path = "/", method = RequestMethod.POST, consumes = "application/json")
    public User register(@RequestBody RegisterUser registerUser, HttpServletRequest request) {
        User user = userService.register(registerUser.username, registerUser.password);
        if (user == null) {
            return null;
        }
        userService.setUserForRequest(request, user.getUsername());
        return user;
    }

    @RequestMapping(path = "/{username}/{password}", method = RequestMethod.GET)
    public User changePasswordForTestuser(@PathVariable("username") String username,
                                          @PathVariable("password") String password) {
        if (!username.equals("test")) {
            return null;
        }
        return userService.changePassword(username, password);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT, consumes = "application/json")
    public User updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        User loggedInUser = checkLoggedIn(request);
        if (!loggedInUser.getId().equals(user.getId()) ||
                !userId.equals(user.getId())) {
            throw new UnauthorizedException("Invalid user");
        }
        userService.updateLanguage(loggedInUser, user.getLanguage());
        userService.updateFollowing(loggedInUser, user.getFollowing());
        return user;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<User> listUsers(HttpServletRequest request) {
        checkLoggedIn(request);
        return userService.getUsers();
    }

    private User checkLoggedIn(HttpServletRequest request) {
        User user = getLoggedInUser(request);
        if (user == null) {
            throw new UnauthorizedException("Not logged in");
        }
        return user;
    }


    public static class RegisterUser {
        String username;
        String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
