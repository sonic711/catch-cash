package com.sean.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "MEMBER")
@Entity
public class MemberEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 使用資料庫自動增加策略
	@Column(name = "ID")
	private Long id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "AGE")
	private Integer age;
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "PASSWORD")
	private String password;
}
