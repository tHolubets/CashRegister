package com.studing.cashRegister.service;

import com.studing.cashRegister.dao.OrderDao;
import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.Order;
import com.studing.cashRegister.model.Report;
import com.studing.cashRegister.util.ConnectionPool;

import java.sql.Connection;
import java.util.List;

/**
 * Class for report management. Singleton
 * @author tHolubets
 */
public class ReportService {
    private OrderDao orderDao = OrderDao.getInstance();
    private static ReportService instance;

    /**
     * Method to get instance of Service (because of Singleton)
     * @return instance of ReportService
     */
    public static synchronized ReportService getInstance(){
        if(instance == null){
            instance = new ReportService();
        }
        return instance;
    }

    private ReportService(){}

    /**
     * Method to make the report
     * @param isZReport set true for Z-report, false - for X-report
     * @return object of report
     * @throws MyException if the DAO class throw it
     */
    public Report makeReport(boolean isZReport) throws MyException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Order> orders = orderDao.getClosedTodayOrders(connection);
        if(isZReport) {
            for (Order order: orders) {
                order.setStatus("registered");
                orderDao.update(connection, order);
            }
        }
        int quantity = 0;
        double sum = 0;
        for (Order order: orders) {
            order.setGoodsList(orderDao.getGoodsByOrderId(connection, order.getId()));
            quantity++;
            sum += order.getTotalAmountDouble();
        }
        ConnectionPool.close(connection);
        return new Report(isZReport?"Z-report":"X-report", quantity, sum);
    }
}
