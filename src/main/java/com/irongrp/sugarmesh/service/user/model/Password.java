package com.irongrp.sugarmesh.service.user.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Password {

    @GeneratedValue
    @Id
    private Long id;

    @Relationship(type = "setBy")
    private User user;

    private String hashedPassword;

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public User getUser() {
        return user;
    }
}
