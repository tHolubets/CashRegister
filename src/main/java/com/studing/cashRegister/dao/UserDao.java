package com.studing.cashRegister.dao;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.User;
import com.studing.cashRegister.model.UserRole;
import com.studing.cashRegister.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Class for Data access object to 'user' table. Singleton.
 * @author tHolubets
 */
public class UserDao {
    private static UserDao instance;
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    /**
     * Method to get instance of DAO (because of Singleton)
     * @return instance of UserDao
     */
    public static synchronized UserDao getInstance(){
        if(instance == null){
            instance = new UserDao();
        }
        return instance;
    }

    private UserDao() {
    }

    /**
     * Method to check user by searching such pair of login and password
     * @param connection communication object to access the database
     * @param user object which should have login and password
     * @return true if there are user with these login parameters, else if not
     * @throws MyException if SQLException are caught
     */
    public boolean checkUserLogin(Connection connection, User user) throws MyException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "SELECT * FROM user WHERE login = ? AND password = ?;";
            statement = connection.prepareStatement(sql);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                UserRole role = UserRole.valueOf(resultSet.getString("role"));
                long id = Long.valueOf(resultSet.getString("user_id"));
                String firstName = resultSet.getString("first_name");
                String surname = resultSet.getString("surname");
                String fatherName = resultSet.getString("father_name");
                user.setId(id);
                user.setRole(role);
                user.setFirstName(firstName);
                user.setSurname(surname);
                user.setFatherName(fatherName);
                return true;
            }
            return false;
        } catch (SQLException throwables) {
            logger.error("Checking user login SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot check user login.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
    }

    /**
     * Method to get all users from database
     * @param connection communication object to access the database
     * @return list of users
     * @throws MyException if SQLException are caught
     */
    public List<User> getAll(Connection connection) throws MyException {
        Statement statement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<>();
        try {
            String sql = "SELECT * FROM user;";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                UserRole role = UserRole.valueOf(resultSet.getString("role"));
                long id = Long.valueOf(resultSet.getString("user_id"));
                String firstName = resultSet.getString("first_name");
                String surname = resultSet.getString("surname");
                String fatherName = resultSet.getString("father_name");
                users.add(new User(id, login, password, role, firstName, surname, fatherName));
            }
        }catch (SQLException throwables) {
            logger.error("Getting all users SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get users.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return users;
    }

    /**
     * Method to get user full name  by his id
     * @param connection communication object to access the database
     * @param userId id of needed user
     * @return user full name (surname + first name + father name)
     * @throws MyException if SQLException are caught
     */
    public String getUserNameById(Connection connection, long userId) throws MyException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String cashierName = "";
        try {
            String sql = "SELECT * FROM user WHERE user_id = ?;";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, userId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String surname = resultSet.getString("surname");
                String fatherName = resultSet.getString("father_name");
                cashierName = surname + " " + firstName + " " + fatherName;
            }
        } catch (SQLException throwables) {
            logger.error("Getting userName by id SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get user by id.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return cashierName;
    }
}
