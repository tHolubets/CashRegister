package com.studing.cashRegister.service;

import com.studing.cashRegister.controller.LoginServlet;
import com.studing.cashRegister.dao.GoodsDao;
import com.studing.cashRegister.dao.OrderDao;
import com.studing.cashRegister.dao.UserDao;
import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.Goods;
import com.studing.cashRegister.model.Order;
import com.studing.cashRegister.model.OrderedGoods;
import com.studing.cashRegister.util.ConnectionPool;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for order management. Singleton
 * @author tHolubets
 */
public class OrderService {
    private OrderDao orderDao = OrderDao.getInstance();
    private GoodsDao goodsDao = GoodsDao.getInstance();
    private UserDao userDao = UserDao.getInstance();
    private static OrderService instance;

    /**
     * Method to get instance of Service (because of Singleton)
     * @return instance of OrderService
     */
    public static synchronized OrderService getInstance(){
        if(instance == null){
            instance = new OrderService();
        }
        return instance;
    }

    private OrderService(){}

    /**
     * Method to save order
     * @param order order to save
     * @return true when execution was successful, false - in exceptional cases
     * @throws MyException if the DAO class throw it
     */
    public boolean save(Order order) throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        if(order.getId()!=0){
            boolean result = orderDao.update(connection, order);
            ConnectionPool.close(connection);
            return result;
        }
        boolean result = orderDao.save(connection, order);
        ConnectionPool.close(connection);
        return result;
    }

    /**
     * Method to add goods to order
     * @param id goods id, id or name are used to identify goods
     * @param name goods name, id or name are used to identify goods
     * @param quantity goods quantity to order, should be non-negative
     * @param order order which goods has to be connected with
     * @return ordered goods object ot null if there are not goods with these name or id
     * @throws MyException if the DAO class throw it
     */
    public OrderedGoods addGoods(long id, String name, int quantity, Order order) throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Goods goods;
        if(id==0){
            goods = goodsDao.getByName(connection, name);
        } else{
            goods = goodsDao.getById(connection, id);
        }
        if(goods == null){
            return null;
        }
        if(quantity>goods.getQuantity()){
            quantity = goods.getQuantity();
        }
        goods.setQuantity(goods.getQuantity() - quantity);
        goodsDao.update(connection, goods);
        OrderedGoods orderedGoods = new OrderedGoods(0, goods.getId(), goods.getName(), goods.getPrice(), quantity);
        if(quantity==0){
            return orderedGoods;
        }
        boolean updated = false;
        for (OrderedGoods presentGoods: order.getGoodsList()) {
            if(presentGoods.getGoodsId()==orderedGoods.getGoodsId()){
                presentGoods.setQuantity(presentGoods.getQuantity()+orderedGoods.getQuantity());
                updated = true;
                orderDao.updateOrderedGoods(connection, presentGoods);
                break;
            }
        }
        if(updated == false) {
            order.getGoodsList().add(orderedGoods);
            orderDao.addGoods( connection, orderedGoods, order.getId());
        }
        ConnectionPool.close(connection);
        return orderedGoods;
    }

    /**
     * Method to get open order by user id
     * @param userId user(cashier) id
     * @return order or null if there are no open orders for this user
     * @throws MyException if the DAO class throw it
     */
    public Order checkForOpenOrders(long userId) throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Order order = orderDao.getOpenOrderByUserId(connection, userId);
        if(order!=null){
            List<OrderedGoods> orderedGoods = orderDao.getGoodsByOrderId(connection, order.getId());
            order.setGoodsList(orderedGoods);
            List<Long> ids = orderedGoods.stream()
                    .map(goods -> goods.getGoodsId())
                    .collect(Collectors.toList());
            List<String> names = goodsDao.getGoodsNamesByIds(connection, ids);
            for (int i = 0; i < orderedGoods.size(); i++) {
                orderedGoods.get(i).setName(names.get(i));
            }
        }
        ConnectionPool.close(connection);
        return order;
    }

    /**
     * Method to get open orders
     * @return list of orders with status 'open'
     * @throws MyException if the DAO class throw it
     */
    public List<Order> checkForOpenOrders() throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Order> orders = orderDao.getOpenOrders(connection);
        for (Order order: orders) {
            List<OrderedGoods> orderedGoods = orderDao.getGoodsByOrderId(connection, order.getId());
            order.setGoodsList(orderedGoods);
            List<Long> ids = orderedGoods.stream()
                    .map(goods -> goods.getGoodsId())
                    .collect(Collectors.toList());
            List<String> names = goodsDao.getGoodsNamesByIds(connection, ids);
            for (int i = 0; i < orderedGoods.size(); i++) {
                orderedGoods.get(i).setName(names.get(i));
            }
        }
        ConnectionPool.close(connection);
        return orders;
    }

    /**
     * Method to get name of cashier who has created the order
     * @param orderId order id
     * @return name of cashier
     * @throws MyException if the DAO class throw it
     */
    public String getOrderCashier(long orderId) throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        long cashierId = orderDao.getOrderCashierId(connection, orderId);
        String cashierName = userDao.getUserNameById(connection, cashierId);
        ConnectionPool.close(connection);
        return cashierName;
    }

    /**
     * Method to close order
     * @param order order to close
     * @return true when execution was successful, false - in exceptional cases
     * @throws MyException if the DAO class throw it
     */
    public boolean closeOrder(Order order) throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        order.setStatus("closed");
        boolean result = order.getGoodsList().size() > 0 ? orderDao.update(connection, order) : orderDao.deleteOrder(connection, order.getId());
        ConnectionPool.close(connection);
        return result;
    }

    /**
     * Method to cancel order
     * @param order order to be canceled
     * @return true when execution was successful, false - in exceptional cases
     * @throws MyException if the DAO class throw it
     */
    public boolean cancelOrder(Order order) throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        for (OrderedGoods orderedGoods: order.getGoodsList()) {
            Goods goods = goodsDao.getById(connection, orderedGoods.getGoodsId());
            goods.setQuantity(goods.getQuantity() + orderedGoods.getQuantity());
            goodsDao.update(connection, goods);
        }
        orderDao.deleteOrderedGoodsByOrderId(connection, order.getId());
        boolean result = orderDao.deleteOrder(connection, order.getId());
        ConnectionPool.close(connection);
        return result;
    }

    /**
     * Method to get order by its id
     * @param id order id
     * @return order object or null if no suitable objects are found
     * @throws MyException if the DAO class throw it
     */
    public Order getById(long id) throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Order order = orderDao.getById(connection, id);
        List<OrderedGoods> orderedGoods = orderDao.getGoodsByOrderId(connection, order.getId());
        order.setGoodsList(orderedGoods);
        List<Long> ids = orderedGoods.stream()
                .map(goods -> goods.getGoodsId())
                .collect(Collectors.toList());
        List<String> names = goodsDao.getGoodsNamesByIds(connection, ids);
        for (int i = 0; i < orderedGoods.size(); i++) {
            orderedGoods.get(i).setName(names.get(i));
        }
        ConnectionPool.close(connection);
        return order;
    }

    /**
     * Method to update quantity of ordered goods in order
     * @param order order which goods positions should be updated
     * @param newQuantityList list of goods quantity in the same order as in order.getOrderedGoods() list
     * @throws MyException if the DAO class throw it
     */
    public void updateOrderedGoodsQuantity(Order order, List<Integer> newQuantityList) throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<OrderedGoods> orderedGoodsList = order.getGoodsList();
        for (int i = 0; i < orderedGoodsList.size(); i++) {
            if(orderedGoodsList.get(i).getQuantity() > newQuantityList.get(i)){
                Goods goods = goodsDao.getById(connection, orderedGoodsList.get(i).getGoodsId());
                int difference = orderedGoodsList.get(i).getQuantity() - newQuantityList.get(i);
                goods.setQuantity(goods.getQuantity()+difference);
                goodsDao.update(connection, goods);
                orderedGoodsList.get(i).setQuantity(newQuantityList.get(i));
                if(orderedGoodsList.get(i).getQuantity() == 0){
                    orderDao.deleteOrderedGoods(connection, orderedGoodsList.get(i).getId());
                }
                else{
                    orderDao.updateOrderedGoods(connection, orderedGoodsList.get(i));
                }
            }
        }
        orderedGoodsList.removeIf(orderedGoods -> orderedGoods.getQuantity()==0);
        ConnectionPool.close(connection);
    }
}
