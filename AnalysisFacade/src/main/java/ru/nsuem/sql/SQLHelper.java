package ru.nsuem.sql;


import ru.nsuem.sql.executor.TResultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;

public class SQLHelper {
    private String connectionString;
    public static void main(String[] args){
        new SQLHelper();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString);
    }
    public <T> T execQuery(String query, TResultHandler<T> handler) throws SQLException {
        Connection con=getConnection();
        try(Statement stmt = con.createStatement()) {
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        } finally {
            con.close();
        }
    }


    public SQLHelper() {
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            connectionString=readConnectionString();
        } catch (SQLException | IOException sqlEx) {
            sqlEx.printStackTrace();
            System.exit(1);
        }
    }

    private String readConnectionString()throws IOException{
        InputStream configFileStream = this.getClass().getResource("/connection.properties").openStream();
        Properties p = new Properties();
        p.load(configFileStream);
        configFileStream.close();
        String instanceName      = (String)p.get("instance");
        String databaseName      = (String)p.get("database");
        String userName      = (String)p.get("user");
        String password      = (String)p.get("password");
        String connection = "jdbc:sqlserver://%1$s;databaseName=%2$s;user=%3$s;password=%4$s;";
        return String.format(connection, instanceName, databaseName,userName,password);
    }
}


