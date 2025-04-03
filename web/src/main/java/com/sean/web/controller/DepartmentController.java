package com.sean.web.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sean.model.repo.DepartmentRepo;

import lombok.RequiredArgsConstructor;

import static com.sean.web.api.ApiPathConstant.BASE_PATH;

@RestController
@RequestMapping(value = BASE_PATH + "/department")
@RequiredArgsConstructor
public class DepartmentController {

	final DepartmentRepo departmentRepo;

//	@DeleteMapping(value = "/{departmentId}")
//	public void deleteMember(@PathVariable Integer departmentId) {
//		departmentRepo.deleteById(departmentId);
//	}
}
