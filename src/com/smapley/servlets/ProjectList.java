package com.smapley.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.smapley.bean.ProUse;
import com.smapley.bean.User;
import com.smapley.db.modes.ProjectMode;
import com.smapley.db.modes.Result;
import com.smapley.db.service.XDAO;
import com.smapley.utils.MyData;

/**
 * Servlet implementation class Login
 */
@WebServlet("/ProjectList")
public class ProjectList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProjectList() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		Result result = new Result();
		try {
			String time = request.getParameter("time");
			System.out.println("--ProjectList--");

			HttpSession session = request.getSession(false);
			if (session != null) {
				User user = (User) session.getAttribute("user");
				List<ProjectMode> projectModes = new ArrayList<ProjectMode>();
				for (ProUse proUse : (List<ProUse>) XDAO.proUseDAO
						.findByProperty("user", user)) {
					projectModes.add(new ProjectMode(proUse.getProject(), Long
							.parseLong(time)));
				}
				// 返回数据
				result.flag = MyData.SUCC;
				result.details = "";
				result.data = JSON.toJSONString(projectModes);

			} else {
				result.flag = MyData.OutLogin;
				result.details = MyData.ERR_OutLogin;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("--ProjectList--result--" + result.flag + "--"
				+ result.details + "--" + result.data);
		out.print(JSON.toJSONString(result));
		out.flush();
		out.close();

	}
}