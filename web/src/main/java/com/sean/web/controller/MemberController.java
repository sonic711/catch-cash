package com.sean.web.controller;

import com.sean.model.entities.MemberEntity;
import com.sean.web.service.MemberService;
import com.sean.web.vo.MemberDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/member")
@RequiredArgsConstructor
@Tag(name = "Member")
public class MemberController {

    private final MemberService mainService;

    // get 查詢 Member 資料
    @Operation(summary = "Get Member by Id",
            description = "查詢該會員的資料",
            tags = {"Member"},
            parameters = {@Parameter(name = "memberId", description = "會員編號", required = true, example = "1")},
            responses = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "Not Found")})
    @GetMapping(value = "/{memberId}")
    public MemberDetailVO getMember(@PathVariable Integer memberId) {
        return mainService.getMember(memberId);
    }

    // get 查詢 Member 資料
    @GetMapping
    public List<MemberEntity> getMembers() {
        return mainService.getMembers();
    }

    @Operation(summary = "Create Member",
            description = "新增會員資料",
            tags = {"Member"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "會員資料", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberEntity.class))),
            responses = {@ApiResponse(responseCode = "200", description = "Created"), @ApiResponse(responseCode = "400", description = "Bad Request")})
    @PostMapping
    public void createMember(MemberEntity member) {
        mainService.saveMember(member);
    }

    @Operation(summary = "Update Member",
            description = "更新會員資料",
            tags = {"Member"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "會員資料", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberEntity.class))),
            responses = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "Bad Request")})
    @PutMapping
    public void updateMember(MemberEntity member) {
        mainService.updateMember(member);
    }

    @Operation(summary = "Delete Member",
            description = "刪除會員資料",
            tags = {"Member"},
            parameters = {@Parameter(name = "memberId", description = "會員編號", required = true, example = "1")},
            responses = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "Not Found")})
    @DeleteMapping(value = "/{memberId}")
    public void deleteMember(@PathVariable Integer memberId) {
        mainService.deleteMember(memberId);
    }

}
