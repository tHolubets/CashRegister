package com.studing.cashRegister.dao;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.User;
import com.studing.cashRegister.model.UserRole;
import com.studing.cashRegister.util.ConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    private static Connection connection = null;
    private UserDao userDao = UserDao.getInstance();
    private static List<User> savedUsers = new ArrayList<>();
    private static List<UserRole> roles = List.of(UserRole.COMMODITY_EXPERT, UserRole.CASHIER, UserRole.SENIOR_CASHIER);

    static{
        Properties props = new Properties();
        String url = "url";
        String userName = "userName";
        String password = "password";
        try (InputStream input = UserDaoTest.class.getClassLoader().getResourceAsStream("config.properties");) {
            props.load(input);
            url = props.getProperty("db.url");
            userName = props.getProperty("db.username");
            password = props.getProperty("db.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    static{
        for (int i = 0; i < 3; i++) {
            savedUsers.add(new User(0, "myLogin"+i, "pass"+i, roles.get(i),
                    "fin"+i, "sn"+i, "fan"+i));
        }
    }

    private static void close(AutoCloseable ac) throws MyException {
        if(ac!=null){
            try {
                ac.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new MyException("Cannot close resources.", ex);
            }
        }
    }


    @BeforeAll
    static void beforeAll() throws MyException {
        PreparedStatement statement = null;
        Statement statement1 = null;
        try {
            long firstId = savedUsers.size();
            String sql = "DELETE FROM user";
            statement1 = connection.createStatement();
            statement1.executeUpdate(sql);
            sql = "INSERT INTO user (login, password, role, first_name, surname, father_name) VALUES (?,?,?,?,?,?), (?,?,?,?,?,?), (?,?,?,?,?,?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < 3; i++) {
                statement.setString(i*6+1, savedUsers.get(i).getLogin());
                statement.setString(i*6+2, savedUsers.get(i).getPassword());
                statement.setString(i*6+3, savedUsers.get(i).getRole().name());
                statement.setString(i*6+4, savedUsers.get(i).getFirstName());
                statement.setString(i*6+5, savedUsers.get(i).getSurname());
                statement.setString(i*6+6, savedUsers.get(i).getFatherName());
            }
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                firstId = generatedKeys.getLong(1);
            } else {
                throw new SQLException("Creating users failed, no ID obtained.");
            }
            for (int i = 0; i < 3; i++) {
                savedUsers.get(i).setId(firstId+i);
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MyException("Cannot save user data.", throwables);
        }finally {
            ConnectionPool.close(statement1);
            ConnectionPool.close(statement);
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getInstance() {
        UserDao userDao1 = UserDao.getInstance();
        UserDao userDao2 = UserDao.getInstance();
        assertEquals(userDao1, userDao2);
        assertSame(userDao1, userDao2);
    }

    @Test
    void checkUserLogin() throws MyException {
        User userForCheck = new User("myLogin0", "pass0");
        assertTrue(userDao.checkUserLogin(connection, userForCheck));
        User userForCheck2 = new User("myLogin1", "pass0");
        assertFalse(userDao.checkUserLogin(connection, userForCheck2));
    }

    @Test
    void getAll() throws MyException {
        List<User> usersFromDb = userDao.getAll(connection);
        assertTrue(usersFromDb.size()==savedUsers.size());
        for (int i = 0; i < usersFromDb.size(); i++) {
            assertEquals(usersFromDb.get(i).getLogin(), savedUsers.get(i).getLogin());
        }
    }

    @Test
    void getUserNameById() throws MyException {
        String name = userDao.getUserNameById(connection, savedUsers.get(0).getId());
        assertEquals(name, savedUsers.get(0).getFullName());
    }
}