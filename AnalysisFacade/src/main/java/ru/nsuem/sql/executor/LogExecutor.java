package ru.nsuem.sql.executor;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


 class LogExecutor {
    private final Connection connection;

     LogExecutor(Connection connection) {
        this.connection = connection;
    }



    Connection getConnection() {
        return connection;
    }
}
