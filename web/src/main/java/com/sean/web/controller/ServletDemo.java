package com.sean.web.controller;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.gson.Gson;
import com.sean.model.dao.member.MemberDao;
import com.sean.model.entities.MemberEntity;
import com.sean.web.vo.CoreBean;
import com.sean.web.vo.MemberDetailVO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/servlet/*")
public class ServletDemo extends HttpServlet {

	private final MemberDao memberDao;

	public ServletDemo(@Qualifier("memberSQL") MemberDao memberDao) {this.memberDao = memberDao;}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String pathInfo = req.getPathInfo();
		//
		if (pathInfo == null || Objects.equals(pathInfo, "/")) {
			List<MemberEntity> members = memberDao.getMembers();
			List<MemberDetailVO> list = new ArrayList<>();
			for (MemberEntity member : members) {
				MemberDetailVO memberDetailVO1 = new MemberDetailVO();
				BeanUtils.copyProperties(member, memberDetailVO1);
				list.add(memberDetailVO1);
			}
			writeJsonBean(resp, list);
		} else {
			try {
				pathInfo = pathInfo.substring(1);
				String[] pathVariables = pathInfo.split("/");
				Integer id = Integer.parseInt(pathVariables[0]);

				List<MemberDetailVO> list = new ArrayList<>();

				List<MemberEntity> members = memberDao.getMembers();
				for (MemberEntity member : members) {
					MemberDetailVO memberDetailVO1 = new MemberDetailVO();
					BeanUtils.copyProperties(member, memberDetailVO1);
					list.add(memberDetailVO1);
				}
				writeJsonBean(resp, list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * POST 新增
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			MemberDetailVO postAtt = json2Bean(req, MemberDetailVO.class);
			System.out.println(postAtt);
			boolean result = true;
			writeJsonBean(resp, new CoreBean(result));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static <B> B json2Bean(HttpServletRequest req, Class<B> classOfBean) {
		try (BufferedReader br = req.getReader()) {
			return new Gson().fromJson(br, classOfBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <B> void writeJsonBean(HttpServletResponse resp, B bean) {
		resp.setContentType("application/json");
		try (PrintWriter pw = resp.getWriter()) {
			pw.print(new Gson().toJson(bean));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
