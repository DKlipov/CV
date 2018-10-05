package ru.main;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ru.beans.Mailer;
import ru.beans.User;
import ru.beans.UserBeanManager;

/**
 * Servlet implementation class MailServlet
 */
@WebServlet("/mail")
public class MailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		if (action.equals("setEnable")) {
			Mailer.start();
			return;
		} else if(action.equals("setDisable")){
			Mailer.stop();
			return;
			
		} else if(action.equals("getList")){
			response.setContentType("application/json");
			Gson gson = new Gson();
			response.getWriter().println(gson.toJson(Mailer.getMailList()));
			return;
		} else if(action.equals("sendTest")){
			Mailer.dispath("Test", "PAKOOVP send you test message.",true);
			return;
		}

		if (request.getParameter("login") == null) {
			response.getWriter().println("not have params");
			System.out.println("Request device is empty");
			return;
		}
		String login = request.getParameter("login");
		if (action.equals("addMain")) {
			String pass = request.getParameter("password");
			String server = request.getParameter("server");
			Mailer.addMainAddress(login, pass, server);
			return;
		} else if (action.equals("addDispath")) {
			Mailer.addDispath(login);
			return;
		} else if (action.equals("delDispath")) {
			Mailer.deleteDispath(login);
			return;
		} 
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
