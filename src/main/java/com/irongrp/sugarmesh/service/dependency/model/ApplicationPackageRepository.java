package com.irongrp.sugarmesh.service.dependency.model;


import com.irongrp.sugarmesh.service.user.model.Password;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationPackageRepository extends CrudRepository<ApplicationPackage, Long> {
}
