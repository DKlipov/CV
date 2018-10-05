package ru.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONObject;

import ru.SQL.ConnectionUtils;

public class UserBeanManager {

	
	public static ArrayList<String> getUserList(){
		Connection connection;

		try {
			connection = ConnectionUtils.getConnection();
			Statement statement;
			String sql;
			sql = "select login from user;";
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			ArrayList<String> ret=new ArrayList<String>();
			while (rs.next()) {
				ret.add(rs.getString(1));
			}
			connection.close();
			return ret;

		} catch (Exception e) {
			return new ArrayList<String>();
		}
	}
	
	public static User getUser(String login){
		if(login==null||login.length()==0){
			return null;
		}
		if(users.containsKey(login)){
			return users.get(login);
		}
		User user=loadUser(login);
		if(user!=null){
			users.put(login, user);
		}
		return user;
	}
	
	private static User loadUser(String login){
		User newUser=null;
		try {
			
			Connection connection = ConnectionUtils.getConnection();
			Connection addConn=ConnectionUtils.getConnection();
			Statement statement;
			String sql = "select id,admin from user where login='"+login+"';";
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next()) {
				newUser=new User();
				newUser.login=login;
				newUser.isAdmin=rs.getInt(2)==1?true:false;
				int id=rs.getInt(1);
				newUser.id=id;
				String sql1="select device.name,userRight.level from userRight inner join device on device.id=userRight.device where user="+Integer.toString(id)+";";
				Statement stat=addConn.createStatement();
				ResultSet rs1=stat.executeQuery(sql1);
				while(rs1.next()){
					newUser.devices.put(rs1.getString(1), rs1.getInt(2));
				}
			}
			connection.close();
			addConn.close();
			

		} catch (Exception e) {
		}
		return newUser;
	}
	
	private static ConcurrentHashMap <String, User> users=new ConcurrentHashMap <String, User>();
		
	public static void setRight(String login,String device,int value){
			User user=getUser(login);
			user.devices.put(device, value);
			try {
				Connection connection = ConnectionUtils.getConnection();
				Statement statement;
				String sql;
				sql="select id from device where name='"+device+"';";
					statement = connection.createStatement();
				int deviceId=0;
				ResultSet rs = statement.executeQuery(sql);
				if (rs.next()) {
					deviceId=rs.getInt(1);
				}
				sql = "select A.id from userRight as A  where A.user="+user.id+" and A.device='"+deviceId+"';";
				statement = connection.createStatement();
				rs = statement.executeQuery(sql);
				if (rs.next()) {
					sql="update userRight set level="+Integer.toString(value)+" where user="+Integer.toString(user.id)+" and device="+Integer.toString(deviceId)+";";
				} else{
					sql="insert into userRight (level,user,device) values ("+Integer.toString(value)+","+Integer.toString(user.id)+","+Integer.toString(deviceId)+");";
				}
				statement = connection.createStatement();
				statement.executeUpdate(sql);
				connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			
	}
		
		public static void createUser(String login,String password,boolean isAdmin){
			try {
				Connection connection = ConnectionUtils.getConnection();
				Statement statement;
				String sql;
				sql="insert into user (login,password,admin) values('"+login+"','"+password+"',"+(isAdmin?'1':'0')+");";
					statement = connection.createStatement();
					statement.executeUpdate(sql);
				connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public static void deleteUser(String login){
			User user=getUser(login);
			try {
				Connection connection = ConnectionUtils.getConnection();
				Statement statement;
				String sql;
				sql="delete from user where id="+Integer.toString(user.id)+";";
					statement = connection.createStatement();
					statement.executeUpdate(sql);
				connection.close();
				users.remove(login);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
}
