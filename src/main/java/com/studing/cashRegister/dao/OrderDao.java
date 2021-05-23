package com.studing.cashRegister.dao;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.Order;
import com.studing.cashRegister.model.OrderedGoods;
import com.studing.cashRegister.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Class for Data access object to 'order' and 'goods_order' tables. Singleton.
 * @author tHolubets
 */
public class OrderDao {
    private static OrderDao instance;
    private static final Logger logger = LoggerFactory.getLogger(OrderDao.class);

    /**
     * Method to get instance of DAO (because of Singleton)
     * @return instance of OrderDao
     */
    public static synchronized OrderDao getInstance(){
        if(instance == null){
            instance = new OrderDao();
        }
        return instance;
    }

    private OrderDao() {
    }

    /**
     * Method to get all orders from database
     * @param connection communication object to access the database
     * @return list of orders
     * @throws MyException if SQLException are caught
     */
    public List<Order> getAll(Connection connection) throws MyException {
        Statement statement = null;
        ResultSet resultSet = null;
        List<Order> orders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM aorder;";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                long id = resultSet.getLong("order_id");
                LocalDateTime dateTime = resultSet.getTimestamp("order_time").toLocalDateTime();
                long cashierId = resultSet.getLong("cashier_id");
                String status = resultSet.getString("order_status");
                orders.add(new Order(id, dateTime, cashierId, status, new ArrayList<>()));
            }
            for (Order order : orders) {
                String sql2 = "SELECT * FROM goods_order WHERE order_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql2);
                preparedStatement.setLong(1, order.getId());
                ResultSet resultSetGoods = preparedStatement.executeQuery();
                while (resultSetGoods.next()) {
                    long id = resultSetGoods.getLong("goods_id");
                    int quantity = resultSetGoods.getInt("goods_quantity");
                    double price = resultSetGoods.getDouble("goods_price");
                    order.getGoodsList().add(new OrderedGoods(id, price, quantity));
                }
            }
        } catch (SQLException throwables) {
            logger.error("Getting all orders SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get orders.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return orders;
    }

    /**
     * Method to save new order
     * @param connection communication object to access the database
     * @param order object to save
     * @return true when execution was successful, false - in exceptional cases
     * @throws MyException if SQLException are caught
     */
    public boolean save(Connection connection, Order order) throws MyException {
        PreparedStatement statement = null;
        try {
            String sql = "INSERT INTO aorder (order_time, cashier_id, order_status) VALUES (?,?,?);";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, order.getDateTime());
            statement.setLong(2, order.getCashierId());
            statement.setString(3, order.getStatus());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        } catch (SQLException throwables) {
            logger.error("Saving order SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot save order.", throwables);
        }finally {
            ConnectionPool.close(statement);
        }
        return true;
    }

    /**
     * Method to add positions of goods to order
     * @param connection communication object to access the database
     * @param orderedGoods new position of goods
     * @param orderId id of order to add goods
     * @return position of added goods with setted id
     * @throws MyException if SQLException are caught
     */
    public OrderedGoods addGoods(Connection connection, OrderedGoods orderedGoods, long orderId) throws MyException {
        PreparedStatement statement = null;
        try {
            String sql = "INSERT INTO goods_order (goods_id, order_id, goods_quantity, goods_price) VALUES (?,?,?,?);";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, orderedGoods.getGoodsId());
            statement.setLong(2, orderId);
            statement.setInt(3, orderedGoods.getQuantity());
            statement.setDouble(4, orderedGoods.getPrice());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderedGoods.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating orderedGoods failed, no ID obtained.");
            }
        } catch (SQLException throwables) {
            logger.error("Adding goods to order SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot add goods to order.", throwables);
        }finally {
            ConnectionPool.close(statement);
        }
        return orderedGoods;
    }

    /**
     * Method to get open order by cashier
     * @param connection communication object to access the database
     * @param userId id of cashier
     * @return order object or null if no suitable objects are found
     * @throws MyException if SQLException are caught
     */
    public Order getOpenOrderByUserId(Connection connection, long userId) throws MyException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Order order = null;
        try {
            String sql = "SELECT * FROM aorder WHERE cashier_id = ? AND order_status = 'open' ORDER BY order_id DESC";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, userId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("order_id");
                LocalDateTime dateTime = resultSet.getTimestamp("order_time").toLocalDateTime();
                long cashierId = resultSet.getLong("cashier_id");
                String status = resultSet.getString("order_status");
                order = new Order(id, dateTime, cashierId, status, new ArrayList<>());
            }
        } catch (SQLException throwables) {
            logger.error("Searching open order by user id SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get order for user.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return order;
    }

    /**
     * Method to get orders with 'open' status
     * @param connection communication object to access the database
     * @return list of orders which are 'open'
     * @throws MyException if SQLException are caught
     */
    public List<Order> getOpenOrders(Connection connection) throws MyException {
        Statement statement = null;
        ResultSet resultSet = null;
        List<Order> orders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM aorder WHERE order_status = 'open' ORDER BY order_id DESC";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                long id = resultSet.getLong("order_id");
                LocalDateTime dateTime = resultSet.getTimestamp("order_time").toLocalDateTime();
                long cashierId = resultSet.getLong("cashier_id");
                String status = resultSet.getString("order_status");
                orders.add(new Order(id, dateTime, cashierId, status, new ArrayList<>()));
            }
        } catch (SQLException throwables) {
            logger.error("Searching open orders SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get open orders.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return orders;
    }

    /**
     * Method to get ordered goods of order by id
     * @param connection communication object to access the database
     * @param orderId if of needed order
     * @return list of ordered goods which belong to order
     * @throws MyException if SQLException are caught
     */
    public List<OrderedGoods> getGoodsByOrderId(Connection connection, long orderId) throws MyException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<OrderedGoods> goodsList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM goods_order WHERE order_id = ?;";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, orderId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("record_id");
                long goodsId = resultSet.getLong("goods_id");
                int quantity = resultSet.getInt("goods_quantity");
                double price = resultSet.getDouble("goods_price");
                goodsList.add(new OrderedGoods(id, goodsId, "", price, quantity));
            }
        } catch (SQLException throwables) {
            logger.error("Getting order by id SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get goods for order.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return goodsList;
    }

    /**
     * Method to update ordered goods
     * @param connection communication object to access the database
     * @param orderedGoods object needed to update
     * @return true when execution was successful, false - in exceptional cases
     * @throws MyException if SQLException are caught
     */
    public boolean updateOrderedGoods(Connection connection, OrderedGoods orderedGoods) throws MyException {
        PreparedStatement statement = null;
        try {
            String sql = "UPDATE goods_order SET goods_quantity = ?, goods_price=? WHERE record_id = ?;";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, orderedGoods.getQuantity());
            statement.setDouble(2, orderedGoods.getPrice());
            statement.setLong(3, orderedGoods.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Updating ordered goods SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot update ordered goods info.", throwables);
        }finally {
            ConnectionPool.close(statement);
        }
        return true;
    }

    /**
     * Method to get id of cashier who has created an order
     * @param connection communication object to access the database
     * @param orderId id of needed order
     * @return id of cashier - creator
     * @throws MyException if SQLException are caught
     */
    public long getOrderCashierId(Connection connection, long orderId) throws MyException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long cashierId = 0;
        try {
            String sql = "SELECT cashier_id FROM aorder WHERE order_id = ?;";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, orderId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                cashierId = resultSet.getLong("cashier_id");
            }
        } catch (SQLException throwables) {
            logger.error("Getting order cashier id SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get cashier id for order.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return cashierId;
    }

    /**
     * Method to update order info
     * @param connection communication object to access the database
     * @param order object to update
     * @return true when execution was successful, false - in exceptional cases
     * @throws MyException if SQLException are caught
     */
    public boolean update(Connection connection, Order order) throws MyException {
        PreparedStatement statement = null;
        try {
            String sql = "UPDATE aorder SET order_time = ?, cashier_id = ?, order_status = ? WHERE order_id = ?;";
            statement = connection.prepareStatement(sql);
            statement.setObject(1, order.getDateTime());
            statement.setLong(2, order.getCashierId());
            statement.setString(3, order.getStatus());
            statement.setLong(4, order.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Updating order SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot update order info.", throwables);
        }finally {
            ConnectionPool.close(statement);
        }
        return true;
    }

    /**
     * Method to delete ordered goods of order by its id
     * @param connection communication object to access the database
     * @param orderId id of order which goods should be deleted
     * @return true when execution was successful, false - in exceptional cases
     * @throws MyException if SQLException are caught
     */
    public boolean deleteOrderedGoodsByOrderId(Connection connection, long orderId) throws MyException {
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM goods_order WHERE order_id = ?;";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, orderId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Deleting ordered goods by order SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot delete ordered goods by order.", throwables);
        }finally {
            ConnectionPool.close(statement);
        }
        return true;
    }

    /**
     * Method to delete ordered goods by its id
     * @param connection communication object to access the database
     * @param recordId id of goods record in database
     * @return true when execution was successful, false - in exceptional cases
     * @throws MyException if SQLException are caught
     */
    public boolean deleteOrderedGoods(Connection connection, long recordId) throws MyException {
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM goods_order WHERE record_id = ?;";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, recordId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Deleting ordered goods by record id SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot delete ordered goods by id.", throwables);
        }finally {
            ConnectionPool.close(statement);
        }
        return true;
    }

    /**
     * Method to delete order info
     * @param connection communication object to access the database
     * @param orderId id of object to delete
     * @return true when execution was successful, false - in exceptional cases
     * @throws MyException if SQLException are caught
     */
    public boolean deleteOrder(Connection connection, long orderId) throws MyException {
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM aorder WHERE order_id = ?;";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, orderId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Deleting order SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot delete order.", throwables);
        }finally {
            ConnectionPool.close(statement);
        }
        return true;
    }

    /**
     * Method to get order by its id
     * @param connection communication object to access the database
     * @param id id of needed order
     * @return order object or null if no suitable objects are found
     * @throws MyException if SQLException are caught
     */
    public Order getById(Connection connection, long id) throws MyException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Order order = null;
        try {
            String sql = "SELECT * FROM aorder WHERE order_id = ?;";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                LocalDateTime dateTime = resultSet.getTimestamp("order_time").toLocalDateTime();
                long cashierId = resultSet.getLong("cashier_id");
                String status = resultSet.getString("order_status");
                order = new Order(id, dateTime, cashierId, status, new ArrayList<>());
            }
        } catch (SQLException throwables) {
            logger.error("Getting order by id SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get order by id.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return order;
    }

    /**
     * Method to get orders with 'closed' status, which was created today
     * @param connection communication object to access the database
     * @return list of orders
     * @throws MyException if SQLException are caught
     */
    public List<Order> getClosedTodayOrders(Connection connection) throws MyException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Date date = new Date(new java.util.Date().getTime());
        List<Order> orders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM aorder WHERE order_time > ? AND order_status = 'closed';";
            statement = connection.prepareStatement(sql);
            statement.setObject(1, date);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("order_id");
                LocalDateTime dateTime = resultSet.getTimestamp("order_time").toLocalDateTime();
                long cashierId = resultSet.getLong("cashier_id");
                String status = resultSet.getString("order_status");
                orders.add(new Order(id, dateTime, cashierId, status, new ArrayList<>()));
            }
        } catch (SQLException throwables) {
            logger.error("Getting closed today orders SQL error");
            throwables.printStackTrace();
            throw new MyException("Cannot get today closed orders.", throwables);
        }finally {
            ConnectionPool.close(resultSet);
            ConnectionPool.close(statement);
        }
        return orders;
    }
}
