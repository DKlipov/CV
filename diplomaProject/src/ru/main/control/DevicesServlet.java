package ru.main.control;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ru.SQL.ConnectionUtils;
import ru.beans.UserBeanManager;
import ru.connect.Device;
import ru.connect.DeviceManager;

/**
 * Servlet implementation class DevicesServlet
 */
@WebServlet("/control/devices")
public class DevicesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DevicesServlet() {
        super();
        try{
        }catch(Exception e){System.out.println(e);}
        // TODO Auto-generated constructor stub
        DeviceManager.getDevice("null");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		if(request.getParameter("device")==null){
			response.getWriter().println("not have");
			System.out.println("Request device is empty");
			return;
		}
		
		if (request.getSession().getAttribute("user") == null
				|| !UserBeanManager.getUser(request.getSession().getAttribute("user").toString()).canRead("robot1"))
			{
			response.getWriter().println("Tou not have permissons");
			System.out.println("Not have permissons");
			}
		
		if (!UserBeanManager.getUser(request.getSession().getAttribute("user").toString()).canWrite("robot1")
				&&(request.getParameter("setPosition")!=null||request.getParameter("action")!=null||request.getParameter("patrolPath")!=null||(request.getParameter("patrol")!=null&&!request.getParameter("patrol").equals("get")))
				)
			{
			response.getWriter().println("Tou not have permissons");
			System.out.println("Not have permissons");
			}
			
		
		if(DeviceManager.containsDevice(request.getParameter("device"))==false){response.getWriter().println("not have "+request.getParameter("device"));return;}
		 JSONObject data=new JSONObject();  
		 JSONArray ar=new JSONArray();
		try{ 
			Device dev=DeviceManager.getDevice(request.getParameter("device"));
			if(request.getParameter("changeState")!=null){
		    	dev.setAlarmState(Integer.parseInt(request.getParameter("changeState")));
		    	return;
		    }  

		    if(request.getParameter("setPosition")!=null){
		    	String s=request.getParameter("setPosition");
		    	
		    	dev.sendMessage("setPosition "+s);
		    	return;
		    }   
		    if(request.getParameter("action")!=null){
		    	String s=request.getParameter("action");
		    	dev.sendMessage(s);
		    	return;
		    }  
			
		    if(request.getParameter("patrol")!=null){
		    	String s=request.getParameter("patrol");
		    	
		    	if(s.equals("start")){
		    		if(dev.getPatrolPath()==null){response.getWriter().println("not find current path");return;}
		    		dev.startPatrol();
		    		response.getWriter().println("{\"status\":\"ok\"}");	
		    	}else{
		    		dev.stopPatrol();
		    	response.getWriter().println("{\"status\":\"ok\"}");
		    	}
		    	return;
		    }   
			
			if(request.getParameter("patrolPath")!=null){
	    	String s=request.getParameter("patrolPath");
	    	
	    	if(s.equals("get")){
	    		if(dev.getPatrolPath()==null){return;}
	    		response.getWriter().println("{\"path\":\""+dev.getPatrolPath()+"\"}");	
	    	}else{
	    	dev.setPatrolPath(s);
	    	response.getWriter().println("{\"status\":\"ok\"}");
	    	}
	    	return;
	    }   
		
		String device = request.getParameter("device");
	      
	       String str;
	       data.put("x",dev.getCoordinate()[0]);
	       data.put("y",dev.getCoordinate()[1]);
	       data.put("angle",dev.getCoordinate()[2]);
	       data.put("updTB",dev.getValues()[0]);
	       data.put("updGO",dev.getValues()[1]);
	       data.put("updVV",dev.getValues()[2]);
	       data.put("updPA",dev.getValues()[3]);
	       data.put("status",dev.getStatus()?"Подключен":"Отключен");
	       data.put("log",Arrays.asList(dev.getLog(10)));
		} catch(Exception e){
	    	  data=new JSONObject();
	    	   data.put("error", "error");
	    	   //System.err.println("Device error");    	   
	       }
	       data.writeJSONString(response.getWriter());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
