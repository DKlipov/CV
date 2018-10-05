package ru.main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import ru.SQL.ConnectionUtils;

/**
 * Servlet implementation class AuthServlet
 */
@WebServlet("/login")
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AuthServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (new String("logout").equals(request.getParameter("action"))) {
			System.out.println("logout");
			request.getSession().setAttribute("user", null);
			request.getServletContext().getRequestDispatcher("/home").forward(request, response);
			return;
		}
		if (request.getParameter("login") != null & request.getParameter("pass") != null) {

			// TODO connection to DB
			Connection connection;

			try {
				connection = ConnectionUtils.getConnection();
				Statement statement;
				String sql;
				String login = request.getParameter("login");
				String password = request.getParameter("pass");
				sql = "select password from user where login='";
				sql = sql + login + "';";
				statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				if (rs.next()) {
					if (rs.getString(1).equals(password)) {
						System.out.println("loginned user: "+login);
						request.getSession().setAttribute("user", login);
						JSONObject a = new JSONObject();
						a.put("status", "ok");
						a.writeJSONString(response.getWriter());
						return;
					}
				}

			} catch (Exception e) {
				System.out.println("crach connection");
				System.out.println(e);
				JSONObject a = new JSONObject();
				a.put("status", "login not found");
				a.writeJSONString(response.getWriter());
			}
		}
		JSONObject a = new JSONObject();
		a.put("status", "login not found");
		a.writeJSONString(response.getWriter());
		System.out.println("not loggined");

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
