package com.sean.web.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class MemberExternalVO {

    private Boolean success;

    private ResultVO result;

    @Data
    public static class ResultVO {

        @SerializedName("resource_id")
        private String resourceId;

        private List<RecordsVO> records;

        @Data
        public static class RecordsVO {

            @SerializedName("機構名稱")  // 這裡的 "機構名稱" 是 JSON 中的属性名稱
            private String organizationName;

            @SerializedName("負責人姓名")
            private String principalName;

            @SerializedName("電話")
            private String phone;
        }
    }

}
