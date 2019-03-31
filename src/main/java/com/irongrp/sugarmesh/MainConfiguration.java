package com.irongrp.sugarmesh;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.irongrp.sugarmesh")
@EnableNeo4jRepositories("com.irongrp.sugarmesh")
@EntityScan("com.irongrp.sugarmesh")
@EnableTransactionManagement
public class MainConfiguration {


}
