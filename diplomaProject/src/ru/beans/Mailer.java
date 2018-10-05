package ru.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;

import ru.SQL.ConnectionUtils;
import ru.main.MailSender;

public class Mailer {

	private static Object syn=new Object();
	private static MailSender sender;
	private volatile static long last= Instant.now().getEpochSecond()-500;
	
	public static boolean state=false;
	
	
	public static void addDispath(String login){
		try (Connection connection = ConnectionUtils.getConnection();){		
			Statement statement;
			String sql;
			sql = "insert into email (login,password,server,state) values('"+login+"','','',2);";
			statement = connection.createStatement();
			statement.executeUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteDispath(String login){
		try (Connection connection = ConnectionUtils.getConnection();){		
			Statement statement;
			String sql;
			sql = "delete from email where login='"+login+"' and state=2;";
			statement = connection.createStatement();
			statement.executeUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addMainAddress(String login, String password, String server){
		
		try (Connection connection = ConnectionUtils.getConnection();){		
			Statement statement;
			String sql;
			sql="delete from email where state=1 or state=0;";
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			
			sql = "insert into email (login,password,server,state) values('"+login+"','"+password+"','"+server+"',0);";
			statement = connection.createStatement();
			statement.executeUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getMailList(){
		ArrayList<String> ret=new ArrayList<String>();
		try (Connection connection = ConnectionUtils.getConnection()){
			Statement statement;
			String sql;
			sql="select login from email where state=2;";
			statement = connection.createStatement();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				ret.add(rs.getString(1));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return ret;
	}
	
	public static boolean isCanSend(){
		try (Connection connection = ConnectionUtils.getConnection()){
			Statement statement;
			String sql;
			sql="select login from email where state=0 or state=1;";
			statement = connection.createStatement();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next()) {
				return true;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void dispath(String subject, String text){
		  synchronized(syn){
		if(!state){return;}
		dispath(subject,text,true);

		}
	}
	
	public static void dispath(String subject, String text, boolean forse){
		  synchronized(syn){
				if(Instant.now().getEpochSecond()-last<300){
					return;
				}
				last= Instant.now().getEpochSecond();
				for(String s:getMailList()){
					send(subject,text,s);
				}
		}
	}
	
	private static void send(String subject, String text, String address){
		if(!state){return;}
		try{
		sender.send(subject, text, "PAKOOVP@mail.com", address);
		} catch(Exception e){
			stop();
		}
	}
	
	static{
		check();
	}
	
	private static void check(){
		state=false;
		try (Connection connection = ConnectionUtils.getConnection()){
			
			Statement statement;
			String sql;
			sql="select login, password, server from email where state=0;";
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next()) {
				sender=new MailSender(rs.getString(1),rs.getString(2),rs.getString(3));
				
			}
			state=true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void start(){
		state=false;
		try (Connection connection = ConnectionUtils.getConnection()){
			
			Statement statement;
			String sql;
			sql="select login, password, server from email where state=0;";
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next()) {
				sender=new MailSender(rs.getString(1),rs.getString(2),rs.getString(3));
				
			}
			sql="update email set state=1 where state=0;";
			statement = connection.createStatement();
			statement.executeUpdate(sql);	
			state=true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void stop(){
		if(!state){return;}
		state=false;
		try (Connection connection = ConnectionUtils.getConnection()){
			
			Statement statement;
			String sql;
			sql="update email set state=0 where state=1;";
			statement = connection.createStatement();
			statement.executeUpdate(sql);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
