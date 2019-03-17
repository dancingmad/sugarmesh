package com.irongrp.sugarmesh.service.dependency.model;

import com.irongrp.sugarmesh.service.user.model.User;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class ApplicationPackage {

    @GeneratedValue
    @Id
    private Long id;

    private String name;

    @Relationship(type = "packages")
    private List<ApplicationPackage> packages;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ApplicationPackage> getPackages() {
        return packages;
    }

    public void setPackages(List<ApplicationPackage> packages) {
        this.packages = packages;
    }
}
