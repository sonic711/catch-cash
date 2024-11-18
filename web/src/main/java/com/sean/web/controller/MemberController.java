package com.sean.web.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sean.model.entities.MemberEntity;
import com.sean.web.service.MemberService;
import com.sean.web.vo.BasicOut;
import com.sean.web.vo.MemberDetailVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import static com.sean.web.api.ApiPathConstant.BASE_PATH;
import static com.sean.web.api.ApiSettingConstant.JPEG;

@RestController
@RequestMapping(value = BASE_PATH + "/member")
@RequiredArgsConstructor
@Tag(name = "Member")
public class MemberController {

	private final MemberService mainService;

	// get 查詢 Member 資料
	@Operation(summary = "Get Member by Id",//
			description = "查詢該會員的資料",//
			tags = { "Member" }, //
			parameters = { @Parameter(name = "memberId", description = "會員編號", required = true, example = "1") },//
			responses = { @ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "Not Found") })//
	@GetMapping(value = "/{memberId}")
	public BasicOut<MemberDetailVO> getMember(@PathVariable Integer memberId) {
		return mainService.getMember(memberId);
	}

	// get 查詢 Member 資料
	@GetMapping
	public BasicOut<List<MemberEntity>> getMembers() {
		return mainService.getMembers();
	}

	@Operation(summary = "Create Member",//
			description = "新增會員資料", //
			tags = { "Member" }, //
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "會員資料", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberEntity.class))), responses = { @ApiResponse(responseCode = "200", description = "Created"), @ApiResponse(responseCode = "400", description = "Bad Request") })
	@PostMapping
	public void createMember(@RequestBody MemberDetailVO member) {
		MemberEntity memberEntity = new MemberEntity();
		// 前端傳進來為Base64編碼的圖片，需將其轉換為byte[]存入資料庫
		BeanUtils.copyProperties(member, memberEntity);
		if (null != member.getProfileImage()) {
			memberEntity.setProfileImage(Base64.getDecoder().decode(member.getProfileImage().split(",")[1]));
		}
		mainService.saveMember(memberEntity);
	}

	@Operation(summary = "Update Member", description = "更新會員資料", tags = { "Member" }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "會員資料", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberEntity.class))), responses = { @ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "Bad Request") })
	@PutMapping
	public void updateMember(@RequestBody MemberEntity member) {
		mainService.updateMember(member);
	}

	@Operation(summary = "Delete Member", description = "刪除會員資料", tags = { "Member" }, parameters = { @Parameter(name = "memberId", description = "會員編號", required = true, example = "1") }, responses = { @ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "Not Found") })
	@DeleteMapping(value = "/{memberId}")
	public void deleteMember(@PathVariable Integer memberId) {
		mainService.deleteMember(memberId);
	}

	// 上傳圖片
	@Operation(summary = "Upload Member Image", description = "上傳會員照片", tags = { "Member" }, parameters = { @Parameter(name = "memberId", description = "會員編號", required = true, example = "1") }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "照片檔案", required = true, content = @Content(mediaType = "multipart/form-data")), responses = { @ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "Bad Request") })
	@PostMapping(value = "/image/{memberId}")
	public void uploadMemberImage(@PathVariable Integer memberId, @RequestParam("file") @RequestBody MultipartFile file) {
		mainService.uploadImg(memberId, file);
	}

	// 下載圖片
	@Operation(summary = "Download Member Image", description = "下載會員照片", tags = { "Member" }, parameters = { @Parameter(name = "memberId", description = "會員編號", required = true, example = "1") }, responses = { @ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "Not Found") })
	@GetMapping(value = "/image/{memberId}")
	public String downloadMemberImage(@PathVariable Integer memberId) {
		byte[] imageBytes = mainService.downloadImg(memberId);
		String base64Image = Base64.getEncoder().encodeToString(imageBytes);
		return JPEG + base64Image;
	}
}
