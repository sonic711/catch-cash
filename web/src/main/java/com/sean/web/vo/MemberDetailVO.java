package com.sean.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
}
