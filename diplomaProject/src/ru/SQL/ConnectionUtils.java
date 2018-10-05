package ru.SQL;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {

	public static Connection getMyConnection() throws SQLException, ClassNotFoundException {
		return MYSQLConnUtils.getMySQLConnection();
	}

	public static Connection getConnection(){
		try {
			return getMyConnection(null, "testdb", null, null);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static Connection getMyConnection(String host,String db,String user,String password) throws SQLException, ClassNotFoundException {
		if(host==null){host="127.0.0.1";}
		if(user==null){user="root";}
		if(password==null){password="qwe123";}
		return MYSQLConnUtils.getMySQLConnection(host,db,user,password);
	}

}