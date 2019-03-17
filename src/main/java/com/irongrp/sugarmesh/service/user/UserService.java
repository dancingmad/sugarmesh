package com.irongrp.sugarmesh.service.user;

import com.google.common.hash.Hashing;
import com.irongrp.sugarmesh.service.exception.GeneralException;
import com.irongrp.sugarmesh.service.exception.ResourceAlreadyExistingException;
import com.irongrp.sugarmesh.service.exception.UnauthorizedException;
import com.irongrp.sugarmesh.service.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String SESSION_USERNAME = "SESSION_USERNAME";
    private UserRepository userRepository;
    private PasswordRepository passwordRepository;

    @Autowired
    UserService(UserRepository userRepository,
                PasswordRepository passwordRepository) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
    }

    public User authenticate(String username, String password) {
        Password pw = passwordRepository.findPasswordByUsername(username);
        if (pw == null) {
            return null;
        }
        String passwordHash = Hashing.sha256().hashString(password,
                StandardCharsets.UTF_8).toString();

        if (!passwordHash.equals(pw.getHashedPassword())) {
            return null;
        }
        return pw.getUser();
    }

    public User changePassword(String username, String password) {
        String passwordHash = Hashing.sha256().hashString(password,
                StandardCharsets.UTF_8).toString();
        Password pw = passwordRepository.findPasswordByUsername(username);
        if (pw == null) {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new GeneralException("No user/password exists for user " + username);
            }
            pw = new Password();
            pw.setUser(user);
        }
        pw.setHashedPassword(passwordHash);
        return passwordRepository.save(pw).getUser();
    }

    public User register(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            throw new ResourceAlreadyExistingException("User "+ username + " does already exist");
        }

        String passwordHash = Hashing.sha256().hashString(password,
                StandardCharsets.UTF_8).toString();
        User user = new User();
        user.setUsername(username);
        user.setLanguage(Language.ENGLISH.name());
        Password pw = new Password();
        pw.setUser(user);
        pw.setHashedPassword(passwordHash);
        return passwordRepository.save(pw).getUser();
    }

    public void updateLanguage(User user, String language) {
        user.setLanguage(language);
        userRepository.save(user);
    }

    public void updateFollowing(User user, List<User> followingList) {
        if (CollectionUtils.isEmpty(followingList)) {
            user.setFollowing(new ArrayList<>());
            return;
        }
        List<User> followingUsers = followingList.parallelStream()
                .map(User::getUsername)
                .map(userRepository::findByUsername)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        user.setFollowing(followingUsers);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }


    public void setUserForRequest(HttpServletRequest request, String username) {
        request.getSession().setAttribute(SESSION_USERNAME, username);
    }

    public User getLoggedInUser(HttpServletRequest request) {
        String sessionUser = (String) request.getSession().getAttribute(SESSION_USERNAME);
        if (StringUtils.isEmpty(sessionUser)) {
            throw new UnauthorizedException("Not authenticated!");
        }
        User user = getUser(sessionUser);
        if (user == null) {
            request.getSession().removeAttribute(SESSION_USERNAME);
            throw new RuntimeException("Unexpected: Already authenticated User missing from Database");
        }
        return user;
    }

    public void removeUserForRequest(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_USERNAME);
    }
}
