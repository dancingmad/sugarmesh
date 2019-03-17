package com.irongrp.sugarmesh.service.dependency.model;

import com.irongrp.sugarmesh.service.user.model.User;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class Application {

    @GeneratedValue
    @Id
    private Long id;

    private String domain; // e.g. org.springframework
    private String name; // e.g. boot

    @Relationship(type = "createdBy")
    private User createdBy;

    @Relationship(type = "beans")
    private List<Bean> beans;

    @Relationship(type = "packages")
    private List<ApplicationPackage> packages;

    public List<ApplicationPackage> getPackages() {
        return packages;
    }

    public void setPackages(List<ApplicationPackage> packages) {
        this.packages = packages;
    }

    public Long getId() {
        return id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bean> getBeans() {
        return beans;
    }

    public void setBeans(List<Bean> beans) {
        this.beans = beans;
    }
}
