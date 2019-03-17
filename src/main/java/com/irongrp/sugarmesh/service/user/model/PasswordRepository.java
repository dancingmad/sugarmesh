package com.irongrp.sugarmesh.service.user.model;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PasswordRepository extends CrudRepository<Password, Long> {
    @Query("match (u:User)<-[s:setBy]-(p:Password) where u.username = {userName} return p,s,u")
    Password findPasswordByUsername(@Param("userName") String userName);
}


