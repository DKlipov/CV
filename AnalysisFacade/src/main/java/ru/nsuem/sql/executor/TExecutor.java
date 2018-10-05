package ru.nsuem.sql.executor;



import ru.nsuem.ServiceManager;
import ru.nsuem.sql.SQLHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TExecutor  {
    private Connection connection;
    private boolean status=false;

    public TExecutor() {
    }



    public void close(){
        if(status){

        }
    }
    public <T> T execQuery(String query, TResultHandler<T> handler) throws SQLException {
        //if(!status){init();}
        try(Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        }
    }

}
