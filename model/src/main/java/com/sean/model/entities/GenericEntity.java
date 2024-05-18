package com.sean.model.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class GenericEntity implements Serializable{

	@Serial
	private static final long serialVersionUID = 8947523181149518228L;

	@Column(name = "CREATOR", length = 20, nullable = false)
	private String createUser;
	@Column(name = "CREATETIME", nullable = false)
	private LocalDateTime createTime;
	@Column(name = "MODIFIER", length = 20)
	private String updateUser;
	@Column(name = "LASTUPDATE")
	private LocalDateTime updateTime;

	@PrePersist
	public void prePersist() {
		if (Objects.isNull(createTime)) {
			this.createTime = LocalDateTime.now();
		}

		// 新增時一律同步更新 UPD_TIME 欄位
		if (Objects.isNull(updateTime)) {
			this.updateTime = this.createTime;
		}

		// 新增時一律同步更新 UPD_USER 欄位
		if (Objects.isNull(updateUser) && Objects.nonNull(createUser)) {
			this.updateUser = this.createUser;
		}

	}

	@PreUpdate
	public void preUpdate() {
		this.updateTime = LocalDateTime.now();
	}

}
