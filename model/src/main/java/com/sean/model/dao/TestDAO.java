package com.sean.model.dao;

import org.springframework.stereotype.Repository;

@Repository
public class TestDAO {
	public String getTest() {
		return "This is a test string.";
	}
}
