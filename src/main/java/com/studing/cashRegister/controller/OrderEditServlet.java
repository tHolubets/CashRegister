package com.studing.cashRegister.controller;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.*;
import com.studing.cashRegister.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for editing open orders
 * @author tHolubets
 */
@WebServlet("/openOrders/edit")
public class OrderEditServlet  extends HttpServlet {
    private OrderService orderService = OrderService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(OrderEditServlet.class);

    /**
     * Method to show list of open order
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Long.valueOf(request.getParameter("id"));
        Order order = null;
        try {
            order = orderService.getById(id);
        } catch (MyException ex) {
            request.setAttribute("errorMessage", ex);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
        logger.debug("Edit order({}) get request", order.getId());

        request.setAttribute("order", order);
        request.getRequestDispatcher("orderEdit.jsp").forward(request, response);
    }

    /**
     * Method to edit quantity of goods at open order
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        long id = 0;
        if(idStr!=null){
            id = Long.valueOf(idStr);
        }
        Order order = null;
        try {
            order = orderService.getById(id);
        } catch (MyException ex) {
            request.getSession().setAttribute("errorMessage", ex);
            response.sendRedirect("../error.jsp");
            return;
        }
        List<Integer> newQuantityList = new ArrayList<>();
        for (int i = 0; i < order.getGoodsList().size(); i++) {
            newQuantityList.add(Integer.valueOf(request.getParameter(String.valueOf(i))));
        }
        try {
            orderService.updateOrderedGoodsQuantity(order, newQuantityList);
        } catch (MyException ex) {
            request.getSession().setAttribute("errorMessage", ex);
            response.sendRedirect("../error.jsp");
            return;
        }
        logger.debug("Edit order({}) post request", order.getId());

        response.sendRedirect("../openOrders");
    }
}
