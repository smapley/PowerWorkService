package com.smapley.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.smapley.bean.Project;
import com.smapley.bean.ProjectDAO;
import com.smapley.bean.Task;
import com.smapley.bean.User;
import com.smapley.bean.UserDAO;
import com.smapley.mode.PTaskEntity;
import com.smapley.mode.Result;
import com.smapley.utils.MyData;

/**
 * Servlet implementation class Login
 */
@WebServlet("/PTask")
public class PTask extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAO();
	private ProjectDAO projectDAO = new ProjectDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PTask() {
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
			String user_id = request.getParameter("user_id");
			String skey = request.getParameter("skey");
			String pro_id = request.getParameter("pro_id");
			System.out.println("--PTask--" + user_id + "--" + pro_id);
			// 根据id查询
			User user = userDAO.findById(Integer.parseInt(user_id));
			if (user != null) {
				// 判断skey
				if (user.getSkey().equals(skey)) {
					List<PTaskEntity> listTask = new ArrayList<PTaskEntity>();
					Project project = projectDAO.findById(Integer
							.parseInt(pro_id));
					if (project != null) {
						for (Task task : (Set<Task>) project.getTasks()) {
							listTask.add(new PTaskEntity(task));
						}
					}

					// 返回数据
					result.flag = MyData.SUCC;
					result.details = "";
					result.data = JSON.toJSONString(listTask);

				} else {
					result.flag = MyData.OutLogin;
					result.details = MyData.ERR_OutLogin;
				}
			} else {
				result.details = MyData.ERR_NoUser;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("--result--" + result.flag + "--" + result.details
				+ "--" + result.data);
		out.print(JSON.toJSONString(result));
		out.flush();
		out.close();

	}
}