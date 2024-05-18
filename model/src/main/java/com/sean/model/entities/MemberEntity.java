package com.sean.model.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(name = "MEMBER")
@Entity
@ToString
@AttributeOverrides({ // todo 解決排序問題
		@AttributeOverride(name = "createUser", column = @Column(name = "CREATOR")),//
		@AttributeOverride(name = "createTime", column = @Column(name = "CREATETIME")),//
		@AttributeOverride(name = "updateUser", column = @Column(name = "MODIFIER")), //
		@AttributeOverride(name = "updateTime", column = @Column(name = "LASTUPDATE")) })
public class MemberEntity extends GenericEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 使用資料庫自動增加策略
	@Column(name = "ID")
	private Integer id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "AGE")
	private Integer age;
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "PASSWORD")
	private String password;
}
