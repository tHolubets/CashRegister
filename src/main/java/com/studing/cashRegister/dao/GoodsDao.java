package com.studing.cashRegister.dao;

import com.studing.cashRegister.controller.LoginServlet;
import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.Goods;
import com.studing.cashRegister.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for Data access object to 'goods' table. Singleton.
 * @author tHolubets
 */
public class GoodsDao{
    private static GoodsDao instance;
    private static final Logger logger = LoggerFactory.getLogger(GoodsDao.class);

    /**
     * Method to get instance of DAO (because of Singleton)
     * @return instance of GoodsDao
     */
    public static synchronized GoodsDao getInstance(){
        if(instance == null){
            instance = new GoodsDao();
        }
        return instance;
    }

    private GoodsDao() {
    }

    /**
     * Method to get all goods from database
     * @param connection communication object to access the database
     * @return all goods as List of Goods
     * @throws MyException if SQLException are caught
     */
    public List<Goods> getAll(Connection connection) throws MyException {
        Statement statement = null;
        ResultSet resultSet = null;
        List<Goods> goods = new ArrayList<>();
        try {
            String sql = "SELECT * FROM goods;";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = Double.valueOf(resultSet.getString("price"));
                int quantity = Integer.valueOf(resultSet.getString("quantity"));
                long id = Long.valueOf(resultSet.getString("goods_id"));
                goods.add(new Goods(id, name, description, price, quantity));
            }
        }catch (SQLException throwables) {
            logger.error("Getting all goods SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get all goods.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return goods;
    }

    /**
     * Method to get Goods object by its id
     * @param connection communication object to access the database
     * @param id id of needed goods
     * @return goods object or null if missing
     * @throws MyException if SQLException are caught
     */
    public Goods getById(Connection connection, long id) throws MyException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Goods goods = null;
        try {
            String sql = "SELECT * FROM goods WHERE goods_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = Double.valueOf(resultSet.getString("price"));
                int quantity = Integer.valueOf(resultSet.getString("quantity"));
                goods = new Goods(id, name, description, price, quantity);
            }
        }catch (SQLException throwables) {
            logger.error("Getting goods by id SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get goods by id.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return goods;
    }

    /**
     * Method to get Goods object by its name
     * @param connection communication object to access the database
     * @param name name of needed goods
     * @return Goods object or null if missing
     * @throws MyException if SQLException are caught
     */
    public Goods getByName(Connection connection, String name) throws MyException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Goods goods = null;
        try {
            String sql = "SELECT * FROM goods WHERE name = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("goods_id");
                String description = resultSet.getString("description");
                double price = Double.valueOf(resultSet.getString("price"));
                int quantity = Integer.valueOf(resultSet.getString("quantity"));
                goods = new Goods(id, name, description, price, quantity);
            }
        }catch (SQLException throwables) {
            logger.error("Getting goods by name SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get goods by name.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return goods;
    }

    /**
     * Method to save goods in database
     * @param connection communication object to access the database
     * @param goods object to save
     * @return true when execution was successful, false - in exceptional cases
     * @throws MyException if SQLException are caught
     */
    public boolean save(Connection connection, Goods goods) throws MyException {
        PreparedStatement statement = null;
        try {
            String sql = "INSERT INTO goods (name, description, price, quantity) VALUES (?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, goods.getName());
            statement.setString(2, goods.getDescription());
            statement.setDouble(3, goods.getPrice());
            statement.setInt(4, goods.getQuantity());
            statement.executeUpdate();
        }catch (SQLException throwables) {
            logger.error("Saving goods SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot save goods.", throwables);
        }finally {
            ConnectionPool.close(statement);
        }
        return true;
    }

    /**
     * Method to update Goods object. Saving in database change of state.
     * @param connection communication object to access the database
     * @param goods object to update
     * @return true when execution was successful, false - in exceptional cases
     * @throws MyException if SQLException are caught
     */
    public boolean update(Connection connection, Goods goods) throws MyException {
        PreparedStatement statement = null;
        try {
            String sql = "UPDATE goods SET name=?, description=?, price=?, quantity=? WHERE goods_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, goods.getName());
            statement.setString(2, goods.getDescription());
            statement.setDouble(3, goods.getPrice());
            statement.setInt(4, goods.getQuantity());
            statement.setLong(5, goods.getId());
            statement.executeUpdate();
        }catch (SQLException throwables) {
            logger.error("Updating goods SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot update goods.", throwables);
        }finally {
            ConnectionPool.close(statement);
        }
        return true;
    }

    /**
     * Method to get goods names by their ids
     * @param connection communication object to access the database
     * @param ids list of goods ids
     * @return list of names in the appropriate order
     * @throws MyException if SQLException are caught
     */
    public List<String> getGoodsNamesByIds(Connection connection, List<Long> ids) throws MyException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> names = new ArrayList<>();
        String sql = "SELECT * FROM goods WHERE goods_id = ?";
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < ids.size(); i++) {
                statement.setLong(1, ids.get(i));
                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    names.add(name);
                }
            }
        }catch (SQLException throwables) {
            logger.error("Getting goods namesby ids SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get goods names by ids.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return names;
    }
}
