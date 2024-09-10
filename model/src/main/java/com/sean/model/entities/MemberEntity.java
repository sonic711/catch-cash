package com.sean.model.entities;

import org.hibernate.annotations.Comment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(name = "MEMBER")
@Entity
@ToString
//@AttributeOverrides({ // todo 解決排序問題
//		@AttributeOverride(name = "createUser", column = @Column(name = "CREATOR")),//
//		@AttributeOverride(name = "createTime", column = @Column(name = "CREATETIME")),//
//		@AttributeOverride(name = "updateUser", column = @Column(name = "MODIFIER")), //
//		@AttributeOverride(name = "updateTime", column = @Column(name = "LASTUPDATE")) })
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
	@Column(name = "IMAGE", columnDefinition = "varbinary(max)")
	private byte[] profileImage;

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE,CascadeType.REMOVE })
	@JoinColumn(name = "D_OID", referencedColumnName = "ID")
	@Comment(value = "部門名稱")
	@ToString.Exclude
	private DepartmentEntity department;
}
