package com.studing.cashRegister.controller;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.*;
import com.studing.cashRegister.service.GoodsService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for order creating and displaying
 * @author tHolubets
 */
@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private OrderService orderService = OrderService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(OrderServlet.class);

    /**
     * Method to show order info
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Order saved = (Order)session.getAttribute("order");
        if(saved==null || !saved.getStatus().equals("open")) {
            Order order = null;
            try {
                order = orderService.checkForOpenOrders(user.getId());
            } catch (MyException ex) {
                request.setAttribute("errorMessage", ex);
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
            if (order != null) {
                logger.debug("Show open order({})", order.getId());
                session.setAttribute("order", order);
            }
        }
        request.getRequestDispatcher("order.jsp").forward(request, response);
    }


    /**
     * Method to create new order
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        session.removeAttribute("addPositionError");
        session.removeAttribute("cashierName");
        Order activeOrder = (Order)session.getAttribute("order");
        if (activeOrder == null || activeOrder.getStatus().equals("closed")) {
            long id = 0;
            LocalDateTime dateTime = LocalDateTime.now();
            long cashierId = user.getId();
            String status = "open";
            Order order = new Order(id, dateTime, cashierId, status, new ArrayList<>());
            try {
                orderService.save(order);
            } catch (MyException ex) {
                session.setAttribute("errorMessage", ex);
                response.sendRedirect("error.jsp");
            }
            logger.debug("Creating new order({})", order.getId());

            session.setAttribute("order", order);
            response.sendRedirect("order");
        }
    }
}
