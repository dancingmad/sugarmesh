package com.irongrp.sugarmesh.service.dependency.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeanRepository extends CrudRepository<Bean, Long> {
}
