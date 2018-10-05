package ru.main;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ru.beans.User;
import ru.beans.UserBeanManager;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("user") == null
				|| !UserBeanManager.getUser(request.getSession().getAttribute("user").toString()).isAdmin) {
			response.getWriter().println("you not admin");
			return;
		}
		if (request.getParameter("action") == null) {
			response.getWriter().println("not have params");
			System.out.println("Request device is empty");
			return;
		}
		String action = request.getParameter("action");
		if (action.equals("get")) {
			response.setContentType("application/json");
			Gson gson = new Gson();
			response.getWriter().println(gson.toJson(UserBeanManager.getUserList()));
			return;
		}

		if (request.getParameter("user") == null) {
			response.getWriter().println("not have params");
			System.out.println("Request device is empty");
			return;
		}
		String user = request.getParameter("user");
		if (action.equals("add")) {
			String pass = request.getParameter("password");
			boolean isAdmin = request.getParameter("admin").equals("1");
			UserBeanManager.createUser(user, pass, isAdmin);
			return;
		} else if (action.equals("change")) {
			String device = request.getParameter("device");
			int value = Integer.parseInt(request.getParameter("value"));
			UserBeanManager.setRight(user, device, value);
			response.setContentType("text/plain");
			response.getWriter().println("success");
			return;
		} else if (action.equals("delete")) {
			UserBeanManager.deleteUser(user);
			if (request.getSession().getAttribute("user").equals(user)) {
				request.getSession().setAttribute("user", null);
			}
			return;
		} else if (action.equals("getRight"))
		{
			String device = request.getParameter("device");
			User us = UserBeanManager.getUser(user);
			response.setContentType("application/json");
			Gson gson=new Gson();
			JsonObject obj=new JsonObject();
			if (!us.devices.containsKey(device)) {
				obj.addProperty("value", 0);
				response.getWriter().println(obj);
			} else {
				obj.addProperty("value", Integer.toString(us.devices.get(device)));
				response.getWriter().println(obj);
			}
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
