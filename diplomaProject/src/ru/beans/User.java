package ru.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import ru.SQL.ConnectionUtils;

public class User {

	public int id;
	public String login;
	public HashMap <String,Integer> devices;
	public boolean isAdmin;
	public User(){
		devices= new HashMap <String,Integer>();
	}
	
	public boolean canRead(String device){
		if(isAdmin){return true;}
		if(devices.containsKey(device)){
			return devices.get(device)>0;
		}
		return false;
	}
	
	public boolean canWrite(String device){
		if(isAdmin){return true;}
		if(devices.containsKey(device)){
			return devices.get(device)>1;
		}
		return false;
	}
}
