package com.sean.model.dao;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDAOTest {

	@Test
	void testGetTest() {
		TestDAO testDAO = new TestDAO();
		assertEquals("This is a test string.", testDAO.getTest());
	}
}
