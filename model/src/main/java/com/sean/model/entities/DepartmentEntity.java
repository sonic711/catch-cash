package com.sean.model.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(name = "DEPARTMENT")
@Entity
@ToString
public class DepartmentEntity extends GenericEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 使用資料庫自動增加策略
	@Column(name = "ID")
	private Integer id;

	@Column(name = "D_NAME")
	private String dName;

	@OneToMany(mappedBy = "department",//
			cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true)
	@ToString.Exclude
	@JsonIgnore
	private List<MemberEntity> members;
}
