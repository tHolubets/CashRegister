package com.studing.cashRegister.service;

import com.studing.cashRegister.dao.UserDao;
import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.User;
import com.studing.cashRegister.util.ConnectionPool;

import java.sql.Connection;
import java.util.List;

/**
 * Class for user management. Singleton
 * @author tHolubets
 */
public class UserService {
    private UserDao userDao = UserDao.getInstance();
    private static UserService instance;

    /**
     * Method to get instance of Service (because of Singleton)
     * @return instance of GoodsService
     */
    public static synchronized UserService getInstance(){
        if(instance == null){
            instance = new UserService();
        }
        return instance;
    }

    private UserService(){}

    /**
     * Method to check user login
     * @param user user object
     * @return true if there are user with these login parameters, else if not
     * @throws MyException if the DAO class throw it
     */
    public boolean checkUserLogin(User user) throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        boolean result = userDao.checkUserLogin(connection, user);
        ConnectionPool.close(connection);
        return result;
    }

    /**
     * Method to get all users
     * @return list of users
     * @throws MyException if the DAO class throw it
     */
    public List<User> getAll() throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<User> result = userDao.getAll(connection);
        ConnectionPool.close(connection);
        return result;
    }
}
