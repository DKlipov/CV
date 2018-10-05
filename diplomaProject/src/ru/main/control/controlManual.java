package ru.main.control;

import java.io.IOException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import ru.beans.UserBeanManager;
import ru.connect.Device;
import ru.connect.DeviceManager;

/**
 * Servlet implementation class controlManual
 */
@WebServlet("/control/manual")
public class controlManual extends HttpServlet {
	private static final long serialVersionUID = 1L;
      private String trn="";
      private String spd="";
      private String mv="";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public controlManual() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("user") == null
				|| !UserBeanManager.getUser(request.getSession().getAttribute("user").toString()).canWrite("robot1"))
			{
			response.getWriter().println("You have not permissons");
			System.out.println("Not have permissons");
			}
		
		response.setContentType("application/json");
		if(request.getParameter("set")!=null){
			if(!request.getParameter("set").equals("manual")){response.getWriter().println("404");return;}
		       JSONObject a=new JSONObject();
		       if(DeviceManager.containsDevice("robot1")==false){return;}
		       a.put("status", "ok");
		       a.writeJSONString(response.getWriter());
			return;
		}
		if(request.getParameter("use")!=null){	
				//System.out.println(request.getParameter("move")+" "+request.getParameter("speed")+" "+request.getParameter("turn"));
		       JSONObject a=new JSONObject();
		       a.put("status", "ok");
		       a.writeJSONString(response.getWriter());
		       response.getWriter().close();	
		       String move ="stop";
		       if(request.getParameter("move").charAt(0)=='1'){
		    	   move="forward";
		       }else if(request.getParameter("move").charAt(0)=='0'){
		    	   move="stop";
		       }else if(request.getParameter("move").charAt(0)=='2'){
		    	   move="backward";
		       }
		       String speed=request.getParameter("speed");
		       String turn=request.getParameter("turn");
		       if(!DeviceManager.containsDevice("robot1")){return;}
		       Device dev=DeviceManager.getDevice("robot1");
		       if(!dev.getStatus()){return;}
		       if(!turn.equals(trn)){
		    	   dev.sendMessage("turn "+turn);
		    	   trn=turn;
		    	   
		       }
		       if(!speed.equals(spd)){
		    	   dev.sendMessage("speed "+speed);
		    	   spd=speed;
		    	   
		       }
		       
		       if(!move.equals(mv)){
		    	   dev.sendMessage("move "+move);
		    	   mv=move;
		    	   
		       }
		       
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
