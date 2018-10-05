package ru.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ru.connect.DeviceManager;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class AuthFilter implements Filter {
	/**
	 * Default constructor.
	 */
	public AuthFilter() {
		// TODO Auto-generated constructor stub
		DeviceManager.init();
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//System.out.println("auth");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(true);		
		String servletPath=req.getServletPath();
		if(servletPath.length()>2&servletPath.indexOf("/", 1)<(0)){
		servletPath=servletPath.substring(1);}
		else if(servletPath.indexOf("/", 1)>=(0)){
		servletPath=servletPath.substring(1,servletPath.indexOf("/", 1));
		}
		else{
			servletPath="null";
		}
		if(servletPath.equals("login")&request.getParameterNames().hasMoreElements()){
			chain.doFilter(request, response);
			return;
		}
		if(servletPath.equals("JavaScript")|servletPath.equals("css")|servletPath.equals("images")){
			chain.doFilter(request, response);
			return;
		}
		if (session.getAttribute("user") != null) {
				chain.doFilter(request, response);
				return;
		}
		request.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp").forward(request, response);
		return;
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
