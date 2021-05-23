package com.studing.cashRegister.util;

import com.studing.cashRegister.exceptions.MyException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class for connection pool creating and managing. Singleton
 * @author tHolubets
 */
public class ConnectionPool {
    private static ConnectionPool instance = null;

    public static ConnectionPool getInstance(){
        if (instance==null)
            instance = new ConnectionPool();
        return instance;
    }

    private ConnectionPool(){}

    public Connection getConnection(){
        Context ctx;
        Connection c = null;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/myPool");
            c = ds.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void close(AutoCloseable ac) throws MyException {
        if(ac!=null){
            try {
                ac.close();
            } catch (Exception ex) {
                //logger.error("Closing connection error");
                ex.printStackTrace();
                throw new MyException("Cannot close resources.", ex);
            }
        }
    }
}
