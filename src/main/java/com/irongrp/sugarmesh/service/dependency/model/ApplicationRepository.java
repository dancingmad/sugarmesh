package com.irongrp.sugarmesh.service.dependency.model;

import com.irongrp.sugarmesh.service.user.model.Password;
import com.irongrp.sugarmesh.service.user.model.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends Neo4jRepository<Application, Long> {

    @Query("MATCH (app:Application) " +
            " WHERE app.createdBy = {user} and app.name = {applicationName} " +
            " detach delete app")
    void deleteApplication(@Param("user") User user, @Param("applicationName") String applicationName);

}
