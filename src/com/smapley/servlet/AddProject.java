package com.smapley.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Transaction;

import com.alibaba.fastjson.JSON;
import com.smapley.HibernateSessionFactory;
import com.smapley.bean.Dynamic;
import com.smapley.bean.DynamicDAO;
import com.smapley.bean.ProUse;
import com.smapley.bean.ProUseDAO;
import com.smapley.bean.ProUseId;
import com.smapley.bean.Project;
import com.smapley.bean.ProjectDAO;
import com.smapley.bean.User;
import com.smapley.bean.UserDAO;
import com.smapley.mode.ProjectEntity;
import com.smapley.mode.Result;
import com.smapley.utils.MyData;

/**
 * Servlet implementation class Login
 */
@WebServlet("/AddProject")
public class AddProject extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAO();
	private ProjectDAO projectDAO = new ProjectDAO();
	private ProUseDAO proUseDAO = new ProUseDAO();
	private DynamicDAO dynamicDAO=new DynamicDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddProject() {
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
			String name = request.getParameter("name");
			System.out.println("--AddProject--" + user_id + "--" + name);
			// 根据id查询
			User user = userDAO.findById(Integer.parseInt(user_id));
			if (user != null) {
				// 判断skey
				if (user.getSkey().equals(skey)) {
					boolean isHad = false;
					for (ProUse prouse : (Set<ProUse>) user.getProUses()) {
						if (prouse.getId().getProject().getName().equals(name)) {
							isHad = true;
							break;
						}
					}
					if (!isHad) {
						// 设置新信息
						Project project = new Project();
						project.setName(name);
						project.setCreDate(new Timestamp(System
								.currentTimeMillis()));
						Transaction treTransaction = HibernateSessionFactory
								.getSession().beginTransaction();
						projectDAO.save(project);
						ProUseId proUseId = new ProUseId();
						proUseId.setProject(project);
						proUseId.setUser(user);
						ProUse prouse = new ProUse();
						prouse.setId(proUseId);
						prouse.setRank(0);
						proUseDAO.save(prouse);
						Dynamic dynamic=new Dynamic();
						dynamic.setUser(user);
						dynamic.setProject(project);
						dynamic.setType(0);
						dynamic.setCreDate(new Timestamp(System.currentTimeMillis()));
						dynamicDAO.save(dynamic);
						treTransaction.commit();
						project = (Project) projectDAO.findByExample(project)
								.get(0);
						// 返回数据
						result.flag = MyData.SUCC;
						result.details = "";
						result.data = JSON.toJSONString(new ProjectEntity(
								project));
					} else {
						result.details = MyData.ERR_ProjectName;
					}
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
