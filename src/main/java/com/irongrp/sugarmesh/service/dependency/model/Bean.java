package com.irongrp.sugarmesh.service.dependency.model;

import com.irongrp.sugarmesh.service.user.model.User;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class Bean {

    private Long id;

    private String name;

    private String fullName;

    @Relationship(type = "belongsToPackage")
    private ApplicationPackage belongsToPackage;

    @Relationship(type = "dependsOn")
    private List<Bean> dependsOn;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplicationPackage getBelongsToPackage() {
        return belongsToPackage;
    }

    public void setBelongsToPackage(ApplicationPackage belongsToPackage) {
        this.belongsToPackage = belongsToPackage;
    }

    public List<Bean> getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(List<Bean> dependsOn) {
        this.dependsOn = dependsOn;
    }
}
