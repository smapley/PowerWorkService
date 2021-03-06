package com.smapley.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.smapley.bean.User;
import com.smapley.db.entity.UserEntity;
import com.smapley.db.modes.Result;
import com.smapley.db.service.XDAO;
import com.smapley.utils.MyData;

@WebServlet("/Register")
public class Register extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Register() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		Result result = new Result();

		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String phone = request.getParameter("phone");
			System.out.println("--注册--" + username + "--" + password);

			// 查询是否存在相同用户名
			List<User> list = XDAO.userDAO.findByUsername(username);
			if (list.isEmpty()) {
				// 创建Session
				HttpSession session = request.getSession();
				// 保存User
				User user = new User();
				user.setUsername(username);
				user.setPassword(password);
				user.setSkey(session.getId());
				user.setTruename(username);
				user.setPicUrl("base/user.png");
				user.setPhone(phone);
				user.setCreDate(new Timestamp(System.currentTimeMillis()));
				user.setBirthday(new Timestamp(System.currentTimeMillis()));
				user.setRefresh(new Timestamp(System.currentTimeMillis()));
				user.setState(0);
				XDAO.userDAO.save(user);
				// 返回
				result.flag = MyData.SUCC;
				result.details = "";
				result.data = JSON.toJSONString(new UserEntity(user));

			} else {
				// 存在相同用户名
				result.details = MyData.ERR_USERNAME;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("--注册--result--" + result.flag + "--"
				+ result.details + "--" + result.data);
		out.print(JSON.toJSONString(result));
		out.flush();
		out.close();
	}
}
