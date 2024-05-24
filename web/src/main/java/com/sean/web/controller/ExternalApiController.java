package com.sean.web.controller;

import com.sean.web.service.ExternalApiService;
import com.sean.web.vo.MemberExternalVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sean.web.api.ApiPathConstant.SYS_PATH;

@RestController
@RequestMapping(value = SYS_PATH + "/external")
@RequiredArgsConstructor
@Tag(name = "External API")
public class ExternalApiController {

    @Autowired
    private ExternalApiService externalApiService;
    @Operation(summary = "取得外部資源用統一入口",
            description = "取得外部資源用統一入口",
            tags = {"External API"},
            parameters = {@Parameter(name = "action", description = "取得外部資源類型", required = true, example = "dataStore")},
            responses = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "Not Found")})
    @GetMapping(value = "/{action}")
    public MemberExternalVO getDataStore(@PathVariable String action) {
        return switch (action) {
            case "dataStore" -> externalApiService.getExternalDataStore();
            // todo: add more actions here
            default -> throw new IllegalArgumentException("Invalid action: " + action);
        };
    }

}
