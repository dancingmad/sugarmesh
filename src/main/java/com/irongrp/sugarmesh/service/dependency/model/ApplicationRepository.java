package com.irongrp.sugarmesh.service.dependency.model;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends Neo4jRepository<Application, Long> {

    @Query("MATCH (app:Application)-->(u:User) " +
            " WHERE u.username = {username} and app.name = {applicationName} " +
            " detach delete app")
    void deleteApplication(@Param("username") String username, @Param("applicationName") String applicationName);

}
