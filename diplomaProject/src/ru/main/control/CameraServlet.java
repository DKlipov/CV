package ru.main.control;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.connect.Device;
import ru.connect.DeviceManager;

/**
 * Servlet implementation class CameraServlet
 */
@WebServlet("/camera")
public class CameraServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CameraServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getParameter("device")==null){
			response.setContentType("text/html");
			response.getWriter().println("bad request");
			return;
		}
		boolean directLink=false;
		if(request.getParameter("direct")!=null&&request.getParameter("direct").equals("true")){
			directLink=true;
		}
		
		Device dev=DeviceManager.getDevice(request.getParameter("device"));
		if(dev==null){
			response.setContentType("text/html");
			response.getWriter().println("not find device");
			return;
		}
		URL url;
		if(directLink){
			url=new URL("http://"+dev.getIp().toString()+":8080/?action=stream");
		} else{
			url=new URL("http://127.0.0.1:"+Integer.toString(dev.getCameraPort()));
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
		
			Map<String, java.util.List<String>> myMap = conn.getHeaderFields();
			for(String k : myMap.keySet()) {
				if(k!=null){
					for(String h :myMap.get(k)){
					response.addHeader(k, h);
					}
				}
			}
			
	         byte[] buffer=new byte[1024];
	            int i=conn.getInputStream().read(buffer);
	            while(i>0){
	                response.getOutputStream().write(buffer,0,i);
	                i=conn.getInputStream().read(buffer);
	            }
	            

		} catch (Exception e) {
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
