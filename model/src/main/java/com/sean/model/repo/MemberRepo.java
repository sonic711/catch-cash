package com.sean.model.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sean.model.entities.MemberEntity;

public interface MemberRepo extends JpaRepository<MemberEntity, Integer> {
	Optional<MemberEntity> findByNameAndPassword(String name, String password);
}
