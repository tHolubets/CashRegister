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
import java.util.List;

/**
 * Servlet for showing open orders
 * @author tHolubets
 */
@WebServlet("/openOrders")
public class OpenOrdersServlet extends HttpServlet {
    private OrderService orderService = OrderService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(OpenOrdersServlet.class);

    /**
     * Method to display open orders
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Get open orders request");

        HttpSession session = request.getSession();
        List<Order> orders = null;
        try {
            orders = orderService.checkForOpenOrders();
        } catch (MyException ex) {
            request.setAttribute("errorMessage", ex);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
        if(orders!=null){
            session.setAttribute("openOrders", orders);
        }
        request.getRequestDispatcher("openOrders.jsp").forward(request, response);
    }
}
