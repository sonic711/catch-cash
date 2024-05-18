package com.sean.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "MemberVO", description = "會員輸入物件")
public class MemberBriefVO {
    @Schema(name = "email", example = "seanliu@gmail.com", description = "使用者信箱", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    @Schema(name = "password", example = "123456", description = "使用者密碼", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
