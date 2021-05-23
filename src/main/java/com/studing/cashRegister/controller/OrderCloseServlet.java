package com.studing.cashRegister.controller;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.Order;
import com.studing.cashRegister.model.Permission;
import com.studing.cashRegister.model.User;
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

/**
 * Servlet for closing orders
 * @author tHolubets
 */
@WebServlet("/order/close")
public class OrderCloseServlet extends HttpServlet {
    private OrderService orderService = OrderService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(OrderCloseServlet.class);

    /**
     * Method to close open order
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        String cashierName = null;
        try {
            cashierName = orderService.getOrderCashier(order.getId());
        } catch (MyException ex) {
            session.setAttribute("errorMessage", ex);
            response.sendRedirect("../error.jsp");
        }
        try {
            orderService.closeOrder(order);
        } catch (MyException ex) {
            session.setAttribute("errorMessage", ex);
            response.sendRedirect("../error.jsp");
        }
        logger.debug("Close order({}) request", order.getId());

        session.setAttribute("cashierName", cashierName);
        response.sendRedirect("../order");
    }
}
