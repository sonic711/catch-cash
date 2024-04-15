package com.sean.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sean.model.entities.MemberEntity;

public interface MemberRepo extends JpaRepository<MemberEntity, Long> {

}
