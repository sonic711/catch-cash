package com.sean.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sean.model.entities.DepartmentEntity;

public interface DepartmentRepo extends JpaRepository<DepartmentEntity, Integer> {

}
