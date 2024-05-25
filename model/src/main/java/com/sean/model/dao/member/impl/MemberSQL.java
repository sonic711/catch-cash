package com.sean.model.dao.member.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.sean.model.dao.member.MemberDao;
import com.sean.model.entities.MemberEntity;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MemberSQL implements MemberDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public MemberEntity getMember(Integer memberId) {
		String sql = "SELECT * FROM member WHERE id = ?";
		RowMapper<MemberEntity> rowMapper = (rs, rowNum) -> {
			MemberEntity member = new MemberEntity();
			member.setId(rs.getInt("ID"));
			member.setPassword(rs.getString("PASSWORD"));
			member.setAge(rs.getInt("AGE"));
			member.setName(rs.getString("NAME"));
			member.setEmail(rs.getString("EMAIL"));
			member.setCreateUser(rs.getString("CREATOR"));
			member.setCreateTime(rs.getTimestamp("CREATETIME").toLocalDateTime());
			member.setUpdateUser(rs.getString("MODIFIER"));
			member.setUpdateTime(rs.getTimestamp("LASTUPDATE").toLocalDateTime());
			return member;
		};
		try {
			return jdbcTemplate.queryForObject(sql, rowMapper, memberId);
		} catch (EmptyResultDataAccessException e) {
			// 如果查詢結果為空，返回空的
			log.debug("getMember exception: {}", e.getLocalizedMessage());
			return new MemberEntity();
		}
	}

	@Override
	public List<MemberEntity> getMembers() {
		log.info("SQL getMembers()");

		List<MemberEntity> list = jdbcTemplate.query("SELECT * FROM member", (rs, rowNum) -> {
			MemberEntity member = new MemberEntity();
			member.setId(rs.getInt("id"));
			member.setName(rs.getString("name"));
			member.setEmail(rs.getString("email"));
			member.setPassword(rs.getString("password"));
			member.setCreateTime(rs.getTimestamp("CREATETIME").toLocalDateTime());
			member.setUpdateTime(rs.getTimestamp("LASTUPDATE").toLocalDateTime());
			member.setCreateUser(rs.getString("CREATOR"));
			member.setUpdateUser(rs.getString("MODIFIER"));
			// todo 剩下的欄位
			return member;
		});
		return list;
	}

	@Override
	public void saveMember(MemberEntity member) {
		jdbcTemplate.update("INSERT INTO member (name, email, password, CREATETIME, LASTUPDATE, CREATOR, MODIFIER) VALUES (?,?,?,?, ?,?,?)",//
				member.getName(), member.getEmail(), member.getPassword(), LocalDateTime.now(), LocalDateTime.now(), "ADMIN", "ADMIN");

	}

	@Override
	public void updateMember(MemberEntity member) {
		jdbcTemplate.update("UPDATE member SET name =?, email =?, password =?, LASTUPDATE = ?, MODIFIER =? WHERE id =?",//
				member.getName(), member.getEmail(), member.getPassword(), LocalDateTime.now(), "ADMIN", member.getId());

	}

	@Override
	public void deleteMember(Integer memberId) {
		jdbcTemplate.update("DELETE FROM member WHERE id =?", memberId);
	}

	@Override
	public void uploadImage(Integer memberId, MultipartFile file) {
		try {
			jdbcTemplate.update("UPDATE member SET image =? ,LASTUPDATE = ? WHERE id =?",//
					file.getBytes(), LocalDateTime.now(), memberId);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] downloadImage(Integer memberId) {
		try {
			return jdbcTemplate.queryForObject("SELECT image FROM member WHERE id = ?",//
					byte[].class, memberId);
		} catch (EmptyResultDataAccessException e) {
			// 如果查詢結果為空，返回空的
			log.debug("downloadImage exception: {}", e.getLocalizedMessage());
			return new byte[0];
		}
	}
}
