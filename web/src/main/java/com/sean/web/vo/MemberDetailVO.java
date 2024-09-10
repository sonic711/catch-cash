package com.sean.web.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.sean.model.entities.DepartmentEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDetailVO {

    @JsonProperty("memberId")
    private Integer id;

    @SerializedName("name")
    private String name;

    private Integer age;

    private String email;

    @SerializedName("password")
    private String password;

    // 前端傳入的圖片
    private String profileImage;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private DepartmentEntity department;
}
